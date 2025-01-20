// components/Notes.jsx
// import React, { useState } from 'react';

// export default function Notes() {
//   const [notes, setNotes] = useState([
//     { id: 1, title: 'Sample Note', content: 'This is a sample note' }
//   ]);

//   return (
//     <div className="p-4">
//       <h2 className="text-2xl font-bold mb-4">Notes</h2>
//       <div className="space-y-4">
//         {notes.map(note => (
//           <div key={note.id} className="border p-4 rounded">
//             <h3 className="font-bold">{note.title}</h3>
//             <p>{note.content}</p>
//           </div>
//         ))}
//       </div>
//     </div>
//   );
// }


import React, { useState } from 'react';
import './Notes.css';  // Import custom CSS

export default function Notes() {
  const [notes, setNotes] = useState([
    { id: 1, title: 'Reminder', content: 'Dont forget the machine learning homework !' },
    { id: 2, title: 'Important Update', content: 'Remember to submit the report by Friday.' },
    { id: 3, title: 'Meeting Notes', content: 'We discussed project timelines and goals.' },
  ]);

  return (
    <div className="notes-container">
      <div className="notes-header">
        {/*<h2>My Notes</h2>*/}
      </div>
      <div className="notes-list">
        {notes.map((note) => (
          <div key={note.id} className="note-card">
            <h3 className="note-title">{note.title}</h3>
            <p className="note-content">{note.content}</p>
            <button className="edit-btn">Edit</button>
          </div>
        ))}
      </div>
    </div>
  );
}
  
