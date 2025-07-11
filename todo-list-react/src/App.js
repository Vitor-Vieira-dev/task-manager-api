import React, { useState, useEffect } from 'react';
import TodoItem from './components/TodoItem';

// Estrutura base da aplicação
export default function App() {
  const [tasks, setTasks] = useState(() => {
    const stored = localStorage.getItem('tasks');
    return stored ? JSON.parse(stored) : [];
  });
  const [filter, setFilter] = useState('all');
  const [search, setSearch] = useState('');

  // Atualiza localStorage sempre que tasks mudar
  useEffect(() => {
    localStorage.setItem('tasks', JSON.stringify(tasks));
  }, [tasks]);

  // Adiciona nova tarefa
  function addTask(e) {
    e.preventDefault();
    const form = e.target;
    const title = form.task.value.trim();
    if (!title) return;
    setTasks([...tasks, { id: Date.now(), title, completed: false }]);
    form.reset();
  }

  // Alterna status de completado
  function toggleComplete(id) {
    setTasks(tasks.map(t => t.id === id ? { ...t, completed: !t.completed } : t));
  }

  // Remove tarefa
  function removeTask(id) {
    setTasks(tasks.filter(t => t.id !== id));
  }

  // Filtra tarefas por status e busca
  const filteredTasks = tasks.filter(t => {
    if (filter === 'completed' && !t.completed) return false;
    if (filter === 'active' && t.completed) return false;
    if (!t.title.toLowerCase().includes(search.toLowerCase())) return false;
    return true;
  });

  return (
    <div className="max-w-xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Lista de Tarefas</h1>

      <form onSubmit={addTask} className="mb-4 flex gap-2">
        <input name="task" className="flex-1 p-2 border rounded" placeholder="Nova tarefa..." />
        <button className="bg-blue-500 text-white px-4 py-2 rounded">Adicionar</button>
      </form>

      <div className="flex gap-2 mb-4">
        <button onClick={() => setFilter('all')} className="px-2 py-1 bg-gray-200 rounded">Todas</button>
        <button onClick={() => setFilter('active')} className="px-2 py-1 bg-gray-200 rounded">Ativas</button>
        <button onClick={() => setFilter('completed')} className="px-2 py-1 bg-gray-200 rounded">Completas</button>
        <input
          className="ml-auto px-2 py-1 border rounded"
          placeholder="Buscar..."
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
      </div>

      <ul className="space-y-2">
        {filteredTasks.map(task => (
          <TodoItem key={task.id} task={task} onToggle={toggleComplete} onDelete={removeTask} />
        ))}
      </ul>
    </div>
  );
}
