package controller;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.GUI_Elements;
import view.MenuWindow;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Controller {

    private final GUI_Elements gui_elements;
    private final MenuWindow view;

    public Controller(MenuWindow view, double windowWidth, double windowHeight) {
        this.view = view;
        System.out.println("Create GUI Elements ...");
        this.gui_elements = new GUI_Elements(windowWidth, windowHeight);
    }

    public Parent createStartMenu(){
        System.out.println("Create Menu ...");
        return gui_elements.createStartMenu(view);
    }

    public Dialog<ArrayList<String>> createDialog(){
        Dialog<ArrayList<String>> dialog = new FixedDialog<>();
        dialog.setTitle("DB");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
        return dialog;
    }

    private static class FixedDialog<R> extends Dialog<R> {
        private static Stage stageFor(Dialog<?> dialog) {
            try {
                Field dialogField = Dialog.class.getDeclaredField("dialog");
                dialogField.setAccessible(true);
                Object baseDialog = dialogField.get(dialog);
                Field stageField = baseDialog.getClass().getDeclaredField("stage");
                stageField.setAccessible(true);
                return (Stage) stageField.get(baseDialog);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        FixedDialog() {
            super();
            Stage stage = stageFor(this);
            EventHandler<WindowEvent> onClose = stage.getOnCloseRequest();
            stage.setOnCloseRequest(event -> {
                onClose.handle(event);
                event.consume();
            });
        }

    }
}
