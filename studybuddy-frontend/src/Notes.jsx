// components/Notes.jsx
import React, { useState } from 'react';

export default function Notes() {
  const [notes, setNotes] = useState([
    { id: 1, title: 'Sample Note', content: 'This is a sample note' }
  ]);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">Notes</h2>
      <div className="space-y-4">
        {notes.map(note => (
          <div key={note.id} className="border p-4 rounded">
            <h3 className="font-bold">{note.title}</h3>
            <p>{note.content}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
