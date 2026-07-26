import controller.StudentController;
import repository.StudentRepository;
import service.StudentServiceImpl;
import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            StudentRepository repo = new StudentRepository();
            StudentServiceImpl service = new StudentServiceImpl(repo);
            StudentController controller = new StudentController(service);
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }
}
