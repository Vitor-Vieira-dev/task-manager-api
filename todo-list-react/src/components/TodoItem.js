import React from 'react';

// Componente de item de tarefa
export default function TodoItem({ task, onToggle, onDelete }) {
  return (
    <li className="flex justify-between items-center bg-white p-2 rounded shadow">
      <span
        className={`${task.completed ? 'line-through text-gray-500' : ''} cursor-pointer`}
        onClick={() => onToggle(task.id)}
      >
        {task.title}
      </span>
      <button onClick={() => onDelete(task.id)} className="text-red-500 hover:text-red-700">Excluir</button>
    </li>
  );
}
