

import React, { useState } from 'react';
import { MessageCircle, X } from 'lucide-react';
import './Chatbot.css'; // Import CSS file

export default function Chatbot() {
  const [isOpen, setIsOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([
    { text: 'Hello! How can I help you?', sender: 'bot' }
  ]);

  const userId = localStorage.getItem('userId'); // Get userId from local storage

  const handleSendMessage = async () => {
    if (message.trim()) {
      // Add user's message to chat
      setMessages((prevMessages) => [
        ...prevMessages,
        { text: message, sender: 'user' }
      ]);

      // Clear input field
      setMessage('');

      console.log('User message:', message);

      try {
        // Make API call to send the message
        const response = await fetch(`http://localhost:8080/api/chatbot/send/${userId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'session-id' : localStorage.getItem('token')

          },
          body: JSON.stringify({ question: message }), // Sending question as required by the API
        });

        // Check if the response is OK
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Assuming the response is a JSON object with the bot's reply
        const data = await response.json();
        console.log('Bot response:', data);

        // Check if the response contains a 'reply'
        if (data && data.answer) {
          // Add bot's response to the chat
          setMessages((prevMessages) => [
            ...prevMessages,
            { text: data.answer, sender: 'bot' }
          ]);
        } else {
          // Handle missing 'reply' field in the response
          setMessages((prevMessages) => [
            ...prevMessages,
            { text: 'Sorry, I didn\'t understand that.', sender: 'bot' }
          ]);
        }
      } catch (error) {
        console.error('Error sending message:', error);
        setMessages((prevMessages) => [
          ...prevMessages,
          { text: 'Sorry, there was an error with the chatbot. Please try again later.', sender: 'bot' }
        ]);
      }
    }
  };

  return (
    <>
      {/* Button to open the chatbot */}
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="chatbot-toggle-btn"
        >
          <MessageCircle size={24} />
        </button>
      )}

      {/* Chatbot side panel */}
      <div className={`chatbot-panel ${isOpen ? 'open' : ''}`}>
        <div className="chatbot-header">
          <h2>Chatbot</h2>
          <button onClick={() => setIsOpen(false)} className="chatbot-close-btn">
            <X size={24} />
          </button>
        </div>
        <div className="chatbot-body">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`chatbot-message ${msg.sender}-message`}
            >
              {msg.text}
            </div>
          ))}
        </div>
        <div className="chatbot-input">
          <input
            type="text"
            placeholder="Type your message..."
            className="chatbot-text-input"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()} // Allow Enter key to send
          />
        </div>
        <button
          onClick={handleSendMessage}
          className="chatbot-send-btn"
        >
          Send
        </button>
      </div>
    </>
  );
}
