package command;

public class HomeCommand implements command {
    private Runnable command;
    public HomeCommand(Runnable command){
        this.command = command;
    }
    @Override
    public void execute() {
        command.run();
    }
}
