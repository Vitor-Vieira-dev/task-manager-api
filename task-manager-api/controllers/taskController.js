let tasks = [];
let nextId = 1;

exports.getAllTasks = (req, res) => {
  res.json(tasks);
};

exports.createTask = (req, res) => {
  const { title } = req.body;
  const newTask = { id: nextId++, title, completed: false };
  tasks.push(newTask);
  res.status(201).json(newTask);
};

exports.updateTask = (req, res) => {
  const { id } = req.params;
  const { title, completed } = req.body;
  const task = tasks.find(t => t.id == id);
  if (task) {
    task.title = title ?? task.title;
    task.completed = completed ?? task.completed;
    res.json(task);
  } else {
    res.status(404).json({ message: 'Tarefa não encontrada' });
  }
};

exports.deleteTask = (req, res) => {
  const { id } = req.params;
  const index = tasks.findIndex(t => t.id == id);
  if (index !== -1) {
    tasks.splice(index, 1);
    res.status(204).send();
  } else {
    res.status(404).json({ message: 'Tarefa não encontrada' });
  }
};
