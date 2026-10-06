import repository.ScheduleRepository;
import service.ScheduleService;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        ScheduleRepository repository = new ScheduleRepository();
        ScheduleService service = new ScheduleService(repository);
        ConsoleUI ui = new ConsoleUI(service);

        ui.start();
    }
}
