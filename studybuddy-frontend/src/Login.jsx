

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom'; // Import Link for navigation


export default function Login({ setIsLoggedIn }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/auth/login', { // Adjust URL for your login endpoint
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
        credentials: 'include', // Important for sending cookies with the request
      });


      if (response.ok) {
        const data = await response.json();
        console.log(data);
        if (data.token) {
          // Store the token in localStorage
          localStorage.setItem('token', data.token);
          localStorage.setItem('userId', data.userId); 
          localStorage.setItem('username',data.username)
          
          setIsLoggedIn(true); // Set login state to true in App.js
          navigate('/'); // Redirect to the main page (Calendar, Notes, Chatbot)
        } else {
          setMessage('Login failed. Please try again.');
        }
      } else {
        setMessage('Login failed. Please check your credentials.');
      }
    } catch (error) {
      setMessage('An error occurred. Please try again.');
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center">
      <form onSubmit={handleLogin} className="w-full max-w-md space-y-4 p-8 bg-white rounded shadow">
        <h2 className="text-3xl font-bold text-center">Login</h2>
        <input 
          type="text" 
          placeholder="Username" 
          value={username} 
          onChange={(e) => setUsername(e.target.value)} 
          className="w-full p-2 border rounded"
          required 
        />
        <input 
          type="password" 
          placeholder="Password" 
          value={password} 
          onChange={(e) => setPassword(e.target.value)} 
          className="w-full p-2 border rounded"
          required 
        />
        <button 
          type="submit"
          className="w-full bg-blue-500 text-white p-2 rounded"
        >
          Login
          </button>
          {message && <p className="text-center text-sm text-gray-600">{message}</p>}
          <div className="text-center mt-4">
            <p>Don't have an account? <Link to="/register" className="text-blue-500">Register here</Link></p>
          </div>

      </form>
    </div>
  );
}


