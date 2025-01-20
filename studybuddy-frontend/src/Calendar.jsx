



import React, { useState, useEffect } from 'react';
import './Calendar.css';

const Calendar = () => {
  const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  const months = Array.from({ length: 12 }, (_, i) => new Date(0, i).toLocaleString('default', { month: 'long' }));
  const [currentMonth, setCurrentMonth] = useState(new Date().getMonth());
  const [currentYear, setCurrentYear] = useState(new Date().getFullYear());
  const [plans, setPlans] = useState({});
  useEffect(() => {
    const initializePlans = async () => {
      await fetchAllPlans();
    };
    
    initializePlans();
  }, []);

  const [selectedDate, setSelectedDate] = useState(null);
  const [newPlan, setNewPlan] = useState({ title: '', startHour: '', endHour: '',description:'' });
  const [errorMessage, setErrorMessage] = useState('');

  const getDaysInMonth = (month, year) => new Date(year, month + 1, 0).getDate();
  const getFirstDayOfMonth = (month, year) => new Date(year, month, 1).getDay();

  const totalDaysInMonth = getDaysInMonth(currentMonth, currentYear);
  const firstDayOfMonth = getFirstDayOfMonth(currentMonth, currentYear);
  const calendarDays = [...Array(firstDayOfMonth)].concat([...Array(totalDaysInMonth)].map((_, i) => i + 1));

  const handleDateClick = (day) => {
    const dateKey = `${currentYear}-${String(currentMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    setSelectedDate({ date: dateKey, plans: plans[dateKey] || [] });
    setErrorMessage('');
  };

  const navigateMonth = (direction) => {
    setCurrentMonth((prev) => {
      if (direction === 'prev') return prev === 0 ? 11 : prev - 1;
      return prev === 11 ? 0 : prev + 1;
    });
    if (direction === 'prev' && currentMonth === 0) setCurrentYear((prev) => prev - 1);
    if (direction === 'next' && currentMonth === 11) setCurrentYear((prev) => prev + 1);
  };

  const navigateYear = (direction) => {
    setCurrentYear((prev) => (direction === 'prev' ? prev - 1 : prev + 1));
  };

  const parseTime = (timeStr) => {
    const [hours, minutes] = timeStr.split(':').map(Number);
    return hours * 60 + minutes;
  };

  const checkOverlap = (newStart, newEnd, existingPlans) => {
    return existingPlans.some((plan) => {
      const eventStart = parseTime(plan.startHour);
      const eventEnd = parseTime(plan.endHour);
      return newStart < eventEnd && newEnd > eventStart;
    });
  };

const handleAddPlan = () => {
  if (!newPlan.title  || !newPlan.endHour || !newPlan.startHour) {
    alert('Please fill out all fields.');
    return;
  }

    // Create Date objects for start and end times
  const startDateTime = new Date(`${selectedDate.date}T${newPlan.startHour}`);
  const endDateTime = new Date(`${selectedDate.date}T${newPlan.endHour}`);

  // const formattedStartDate = startDateTime.toISOString().slice(0, 19); utc time 
  // const formattedEndDate = endDateTime.toISOString().slice(0, 19);utc time 


  // Format the dates to ISO 8601 strings (local time)
  const pad = (number) => String(number).padStart(2, '0');

  // Format the date without UTC conversion
  const formattedStartDate = `${startDateTime.getFullYear()}-${pad(startDateTime.getMonth() + 1)}-${pad(startDateTime.getDate())}T${pad(startDateTime.getHours())}:${pad(startDateTime.getMinutes())}:${pad(startDateTime.getSeconds())}`;
  const formattedEndDate = `${endDateTime.getFullYear()}-${pad(endDateTime.getMonth() + 1)}-${pad(endDateTime.getDate())}T${pad(endDateTime.getHours())}:${pad(endDateTime.getMinutes())}:${pad(endDateTime.getSeconds())}`;


  console.log(formattedStartDate,formattedEndDate);

  const newPlanDetails = {
    title: newPlan.title,
    description:"no description",
    startDate: formattedStartDate,
    endDate: formattedEndDate,
  };
  addPlan(newPlanDetails);
  setNewPlan({ title: '', description: '', startHour: '', endHour: '' });

};




  // Placeholder API methods
  const addPlan = async (plan) => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage.');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/user-events/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'session-id' : localStorage.getItem('token')

        },
        body: JSON.stringify(plan),
      });

      if (!response.ok) {
        throw new Error('Failed to add plan');
      }
      const newPlan = await response.json();
      const dateKey = new Date(plan.startDate).toISOString().split("T")[0];
      //console.log("Extracted Date:", dateKey);
      const extractHourFromTimestamp = (timestamp) => {
          const date = new Date(timestamp);
          const pad = (number) => String(number).padStart(2, '0');
          return `${pad(date.getHours())}:${pad(date.getMinutes())}`;
      };

      const updatedPlans = { ...plans };
      if (!updatedPlans[dateKey]) updatedPlans[dateKey] = [];
      const newPlanDetails = {
        title: plan.title,
        description:"no description",
        startHour:extractHourFromTimestamp( plan.startDate),
        endHour: extractHourFromTimestamp( plan.endDate),
      };
      updatedPlans[dateKey].push(newPlanDetails);
      setPlans(updatedPlans); 
      setSelectedDate({ ...selectedDate, plans: updatedPlans[dateKey] });
    } catch (error) {
      console.error('Error adding plan:', error);
    }
  };

  const handleDeletePlan = async (plan) => {
    console.log(plan);
    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage.');
      return;
    }

    const encodedTitle = encodeURIComponent(plan.title);


    try {
      const response = await fetch(`http://localhost:8080/api/user-events/${userId}/${encodedTitle}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'session-id' : localStorage.getItem('token')
        }
      });

      if (!response.ok) {
        throw new Error('Failed to delete plan');
      }

      // Update local state after successful deletion
      const updatedPlans = { ...plans };



      const dateKey = selectedDate;

      console.log("datekey: ",dateKey);

      
      if (updatedPlans[dateKey]) {
        // Filter out the plan that matches both start and end dates
        updatedPlans[currentDateKey] = updatedPlans[DateKey].filter(existingPlan => 
          !(existingPlan.title===plan.title && existingPlan.startHour === plan.startHour && existingPlan.endHour === plan.endHour)
        );
        
        if (updatedPlans[dateKey].length === 0) {
          delete updatedPlans[dateKey];
        }
      }

      setPlans(updatedPlans);
      
      // Update selected date view
      setSelectedDate({
        ...selectedDate,
        plans: updatedPlans[dateKey] || []
      });

    } catch (error) {
      console.error('Error deleting plan:', error);
      alert('Failed to delete plan. Please try again.');
    }
  };
 

  // Fetch all plans for a user
  const fetchAllPlans = async () => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage.');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/user-events/${userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'session-id' : localStorage.getItem('token')
        }
      });

      if (!response.ok) {
        throw new Error('Failed to fetch plans');
      }

      const data = await response.json();
      
      // Transform the received data into the format expected by the calendar
      const transformedPlans = {};
      data.events.forEach(event => {
        const startDate = new Date(event.startDate);
        const endDate = new Date(event.endDate);
        const dateKey = startDate.toISOString().split('T')[0];
        
        if (!transformedPlans[dateKey]) {
          transformedPlans[dateKey] = [];
        }
        
        transformedPlans[dateKey].push({
          title: event.title,
          startHour: startDate.toTimeString().slice(0, 5),
          endHour: endDate.toTimeString().slice(0, 5)
        });
      });
      
      setPlans(transformedPlans);
    } catch (error) {
      console.error('Error fetching plans:', error);
    }
  };


  return (
    <div className="calendar-container">
      <div className="calendar-navigation">
        <button onClick={() => navigateYear('prev')}>&laquo; Year</button>
        <button onClick={() => navigateMonth('prev')}>&laquo; Month</button>
        <span className="calendar-month-year">
          {months[currentMonth]} {currentYear}
        </span>
        <button onClick={() => navigateMonth('next')}>Month &raquo;</button>
        <button onClick={() => navigateYear('next')}>Year &raquo;</button>
      </div>

      <div className="calendar-grid">
        {days.map((day) => (
          <div key={day} className="calendar-day">
            {day}
          </div>
        ))}
        {calendarDays.map((day, index) => {
          const dateKey = `${currentYear}-${String(currentMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
          const hasPlans = plans[dateKey]?.length > 0;

          return (
            <div
              key={index}
              className={`calendar-day-cell ${hasPlans ? 'has-plans' : ''}`}
              onClick={() => day && handleDateClick(day)}
            >
              {day || ''}
            </div>
          );
        })}
      </div>

      {selectedDate && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Plans for {selectedDate.date}</h3>
            <ul>
              {selectedDate.plans.length > 0 ? (
                selectedDate.plans.map((plan, index) => (
                  <li key={index}>
                    <strong>{plan.title}</strong>: {plan.startHour} - {plan.endHour}
                    <button onClick={() => handleDeletePlan(plan)} className="delete-plan-button">x</button>
                  </li>
                ))
              ) : (
                <li>No plans for this day.</li>
              )}
            </ul>
            <div className="plan-input-container">
              <input
                type="text"
                placeholder="Title"
                className="modal-input"
                value={newPlan.title}
                onChange={(e) => setNewPlan({ ...newPlan, title: e.target.value })}
              />
              <input
                type="time"
                placeholder="Start Hour"
                className="modal-input"
                value={newPlan.startHour}
                onChange={(e) => setNewPlan({ ...newPlan, startHour: e.target.value })}
              />
              <input
                type="time"
                placeholder="End Hour"
                className="modal-input"
                value={newPlan.endHour}
                onChange={(e) => setNewPlan({ ...newPlan, endHour: e.target.value })}
              />
              <button className="modal-button" onClick={handleAddPlan}>
                Add Plan
              </button>
              {errorMessage && <div className="error-message">{errorMessage}</div>}
            </div>
            <button className="modal-close-button" onClick={() => setSelectedDate(null)}>
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Calendar;
