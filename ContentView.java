import javax.swing.*;
import java.awt.*;

public class ContentView extends JFrame {
    private final QuestionStore store = new QuestionStore();
    private final CardLayout layout = new CardLayout();
    private final JPanel container = new JPanel(layout);

    public ContentView() {
        setTitle("Gestionnaire de Questions");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        QuestionListView listView = new QuestionListView(store);
        AddQuestionView addView = new AddQuestionView(store, listView::refresh);
        EditQuestionView editView = new EditQuestionView(store, listView::refresh);
        StudyView studyView = new StudyView(store);

        container.add(listView, "list");
        container.add(addView, "add");
        container.add(editView, "edit");
        container.add(studyView, "study");

        setJMenuBar(createMenuBar());
        add(container);

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem viewList = new JMenuItem("Toutes les questions");
        JMenuItem add = new JMenuItem("Ajouter");
        JMenuItem edit = new JMenuItem("Modifier");
        JMenuItem study = new JMenuItem("Étudier");

        viewList.addActionListener(e -> layout.show(container, "list"));
        add.addActionListener(e -> layout.show(container, "add"));
        edit.addActionListener(e -> layout.show(container, "edit"));
        study.addActionListener(e -> layout.show(container, "study"));

        menu.add(viewList);
        menu.add(add);
        menu.add(edit);
        menu.add(study);

        bar.add(menu);
        return bar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContentView::new);
    }
}
