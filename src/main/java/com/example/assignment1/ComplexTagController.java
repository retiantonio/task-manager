package com.example.assignment1;

import dataModel.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ComplexTagController {

    private Task complexTaskObject;
    @FXML private Label complexTagNameLabel;

    public void restoreComplexTag(Task task) {
        complexTaskObject = task;

        complexTagNameLabel.setText(complexTaskObject.getNameTask());
    }

    public void updateNameTag() {
        complexTagNameLabel.setText(complexTaskObject.getNameTask());
    }
}
