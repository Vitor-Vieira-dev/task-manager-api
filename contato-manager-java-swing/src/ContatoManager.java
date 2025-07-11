import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

// Gerenciador de Contatos com Interface Swing e SQLite
public class ContatoManager extends JFrame {
    private JTextField nomeField, telefoneField;
    private DefaultTableModel tableModel;
    private JTable table;
    private Connection conn;

    public ContatoManager() {
        super("Gerenciador de Contatos");
        conectarBanco();
        criarInterface();
        carregarContatos();
    }

    // Conecta ao banco SQLite (ou cria se não existir)
    private void conectarBanco() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:contatos.db");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS contatos (id INTEGER PRIMARY KEY, nome TEXT, telefone TEXT)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados.");
        }
    }

    // Cria a interface gráfica
    private void criarInterface() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        formPanel.add(telefoneField);

        JButton adicionarBtn = new JButton("Adicionar");
        adicionarBtn.addActionListener(e -> adicionarContato());
        formPanel.add(adicionarBtn);

        JButton deletarBtn = new JButton("Deletar Selecionado");
        deletarBtn.addActionListener(e -> deletarContato());
        formPanel.add(deletarBtn);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Telefone"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    // Carrega os contatos existentes do banco
    private void carregarContatos() {
        try {
            tableModel.setRowCount(0); // limpa a tabela
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contatos");
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("nome"), rs.getString("telefone")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar contatos.");
        }
    }

    // Adiciona um novo contato ao banco
    private void adicionarContato() {
        String nome = nomeField.getText().trim();
        String telefone = telefoneField.getText().trim();
        if (nome.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO contatos (nome, telefone) VALUES (?, ?)");
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.executeUpdate();
            nomeField.setText("");
            telefoneField.setText("");
            carregarContatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar contato.");
        }
    }

    // Deleta o contato selecionado na tabela
    private void deletarContato() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um contato para deletar.");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM contatos WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            carregarContatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar contato.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContatoManager::new);
    }
}
