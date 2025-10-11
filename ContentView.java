import javax.swing.*;
import java.awt.*;

public class ContentView extends JPanel {
    private final JPanel centerPanel;
    private final QuestionStore questionStore = new QuestionStore();

    public ContentView() {
        setLayout(new BorderLayout());

        // Menu principal
        JMenuBar menuBar = new JMenuBar();

        // Menu "Menu"
        JMenu craftion = new JMenu("Menu");

        JMenuItem addQuestion = new JMenuItem("Ajouter une Question");
        addQuestion.addActionListener(e -> {
            AddQuestionView view = new AddQuestionView(questionStore);
            JDialog dialog = new JDialog((Frame) null, "Ajouter une Question", true);
            dialog.setContentPane(view);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        JMenuItem playQuestions = new JMenuItem("Jouer les Questions");
        playQuestions.addActionListener(e -> {
            JPanel playView = new PlayQuestionsView(questionStore, this::setCenterPanel);
            setCenterPanel(playView);
        });

        JMenuItem viewQuestions = new JMenuItem("Voir les Questions");
        viewQuestions.addActionListener(e -> {
            JPanel view = new ViewQuestionsView(questionStore);
            setCenterPanel(view);
        });

        JMenuItem questionsExample = new JMenuItem("Exemples de Questions");
        questionsExample.addActionListener(e -> {
            Question[] temp = {
                new Question("1+1", "2"),
                new Question("1+2", "3"),
                new Question("1+3", "4"),
                new Question("1+4", "5"),
                new Question("1+5", "6"),
                new Question("1+6", "7")
            };
            JPanel example = new QuestionsExample(temp);
            setCenterPanel(example);
        });

        JMenuItem editQuestions = new JMenuItem("Éditeur de fichiers clist/txt");
        editQuestions.addActionListener(e -> {
            setCenterPanel(new JLabel("Chargement de l’éditeur...", SwingConstants.CENTER));
            SwingUtilities.invokeLater(() -> {
                JPanel editor = new FileEdit();
                setCenterPanel(editor);
            });
        });

        craftion.add(addQuestion);
        craftion.add(playQuestions);
        craftion.add(viewQuestions);
        craftion.addSeparator();
        craftion.add(editQuestions);

        // Menu "Commandes"
        JMenu cmds = new JMenu("Commandes");
        JMenuItem commands = new JMenuItem("Commandes");
        commands.addActionListener(e -> {
            JPanel command = new CommandView();
            setCenterPanel(command);
        });

        JMenuItem cmdsList = new JMenuItem("Liste de commandes");
        cmdsList.addActionListener(e -> {
            String[] cmdsTable = { "/say Hello World", "/clear" };
            JPanel commandsList = new CommandsList(cmdsTable);
            setCenterPanel(commandsList);
        });

        cmds.add(commands);
        cmds.add(cmdsList);

        // Menu "À propos"
        JMenu about = new JMenu("À propos");
        JMenuItem infos = new JMenuItem("Informations sur l'app");
        infos.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this,
                "Version: 1.7\nDéveloppeur: Tabarcraft\n\nNotes:\n1) OpenCraft est créé par un développeur indépendant de 14 ans.\n2) MLInfo est un secret.",
                "Informations sur OpenCraft",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        JMenuItem settings = new JMenuItem("Réglages");
        settings.addActionListener(e -> {
            JPanel settingsView = new SettingsView();
            setCenterPanel(settingsView);
        });

        JMenuItem quit = new JMenuItem("Quitter");
        quit.addActionListener(e -> System.exit(0));

        about.add(infos);
        about.add(settings);
        about.add(quit);

        // Barre de menus complète
        menuBar.add(craftion);
        menuBar.add(cmds);
        menuBar.add(about);

        // Zone centrale
        centerPanel = new JPanel(new BorderLayout());
        JLabel defaultLabel = new JLabel(
            "<html><div style='text-align:center; font-size:16px; padding:20px;'>Bienvenue dans OpenCraft !<br>Sélectionne une option dans le menu pour commencer.</div></html>",
            SwingConstants.CENTER
        );
        centerPanel.add(defaultLabel, BorderLayout.CENTER);

        // Ajout dans la vue principale
        add(menuBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    // Change dynamiquement la vue centrale
    private void setCenterPanel(JPanel panel) {
        centerPanel.removeAll();
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}
