package com.neutron.controllers;

import com.neutron.Element;
import com.neutron.Shared;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import static com.neutron.Main.db;

public class Foo {
    TableView<Element> table = Shared.table;
    public Foo() {
    }
    public ObservableList<Element> setTableData() {

        final ObservableList<Element> data = FXCollections.observableArrayList();
        TableColumn<Element, String> linkCol = new TableColumn<>("link");
        linkCol.setMinWidth(130);
        linkCol.setCellFactory(TextFieldTableCell.forTableColumn());
        linkCol.setCellValueFactory(new PropertyValueFactory<Element, String>("link"));

        linkCol.setOnEditCommit(
                t -> {
                    Element actualElement = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualElement.setLink(t.getNewValue());
                    db.updateHistory(actualElement);
                }
        );

        TableColumn<Element, String> dateCol = new TableColumn<>("date");
        dateCol.setMinWidth(130);
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setCellValueFactory(new PropertyValueFactory<Element, String>("date"));

        dateCol.setOnEditCommit(
                t -> {
                    Element actualElement = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualElement.setDate(t.getNewValue());
                    db.updateHistory(actualElement);
                }
        );


        TableColumn<Element, String> removeCol = new TableColumn<>("Törlés");
        Callback<TableColumn<Element, String>, TableCell<Element, String>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Element, String> call(final TableColumn<Element, String> param) {
                        return new TableCell<>() {
                            final Button btn = new Button("Törlés");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) -> {
                                        Element Element = getTableView().getItems().get(getIndex());
                                        data.remove(Element);
                                        db.removeElement(Element);
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                    }
                };

        removeCol.setCellFactory( cellFactory );

        table.getColumns().addAll(linkCol, dateCol, removeCol);

        data.addAll(db.getAllElements());

        table.setItems(data);
        return data;
    }
}
