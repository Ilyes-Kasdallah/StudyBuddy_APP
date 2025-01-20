import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import Navbar from './Navbar';
import Calendar from './Calendar';
import Notes from './Notes';
import Chatbot from './Chatbot';

const MainLayout = () => {
  const [currentView, setCurrentView] = useState('calendar');
  return (
    <div className="flex">
      <div className="flex-1 min-h-screen">
        <Navbar onViewChange={setCurrentView} />
        <div className="p-4">
          {currentView === 'calendar' ? <Calendar /> : <Notes />}
        </div>
      </div>
      <Chatbot />
    </div>
  );
};

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Check login status when the app loads
  useEffect(() => {
    const token = localStorage.getItem('token'); // Or check cookies/session
    if (token) {
      setIsLoggedIn(true); // Update the state if the token exists
    }
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={isLoggedIn ? <Navigate to="/" /> : <Login setIsLoggedIn={setIsLoggedIn} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={isLoggedIn ? <MainLayout /> : <Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}
