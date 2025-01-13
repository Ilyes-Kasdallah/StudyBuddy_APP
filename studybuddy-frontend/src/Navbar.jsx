


import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation

export default function Navbar({ onViewChange }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');

  useEffect(() => {
    // Get the username from localStorage when the component mounts
    const storedUsername = localStorage.getItem('username');
    if (storedUsername) {
      setUsername(storedUsername);
    }
  }, []);

  const handleLogout = () => {
    // Clear session or token (e.g., removing from localStorage or cookies)
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
    localStorage.removeItem('username'); // Optionally clear the username as well

    // Redirect to login page
    navigate('/login');
  };

  return (
    <nav className="navbar p-3 shadow-lg flex justify-between items-center rounded-lg">
      <div className="flex space-x-6">
        <button onClick={() => onViewChange('calendar')} className="nav-btn">
          Calendar
        </button>
        <button onClick={() => onViewChange('notes')} className="nav-btn">
          Notes
        </button>
      </div>
      <div className="flex items-center space-x-6"> {/* Increased space-x to 6 */}
        {username && (
          <span className="username-text text-sm font-semibold text-gray-800">
            {username}
          </span>
        )}
        <br/>
        <button onClick={handleLogout} className="logout-btn">
          Logout
        </button>
      </div>
    </nav>
  );
}
