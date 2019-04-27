package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class Controller {
    @FXML
    TextField minSupTxt;
    @FXML
    TextField minConfTxt;

    @FXML
    Button calculateBtn;

    @FXML
    HBox associationPane;
    @FXML
    HBox supersetPane;


    public void calculate() {

        associationPane.getChildren().clear();
        supersetPane.getChildren().clear();

        ArrayList<String> items = new ArrayList<>();
        items.add("skirt");
        items.add("blouse");
        items.add("socks");
        items.add("boots");
        items.add("dress");
        items.add("sneakers");
        items.add("tshirt");
        items.add("trousers");
        items.add("shoes");
        items.add("cap");

        double minSup = Double.parseDouble(minSupTxt.getText());
        double minConf = Double.parseDouble(minConfTxt.getText());

        Apriori apriori = new Apriori(minSup, minConf);

        TableView table = new TableView();



        TableColumn transactionIdCol = new TableColumn("TID");
        TableColumn transactionCol = new TableColumn("ITEMS");

        transactionIdCol.setCellValueFactory(
                new PropertyValueFactory<TableRow,String>("col1")
        );
        transactionCol.setCellValueFactory(
                new PropertyValueFactory<TableRow,String>("col2")
        );

        table.getColumns().addAll(transactionIdCol, transactionCol);

        associationPane.getChildren().add(table);

        ObservableList<TableRow> data = FXCollections.observableArrayList();

        for (int i = 0; i < apriori.transactions.size(); i++) {
            TableRow tableRow = new TableRow(String.valueOf(i+1), apriori.transactions.get(i));

            data.add(tableRow);
        }

        table.setItems(data);

        TableView table2 = new TableView();
        ObservableList<TableRow> data2 = FXCollections.observableArrayList();

        TableColumn itemCol = new TableColumn("Item Set");
        TableColumn freqCol = new TableColumn("count");

        itemCol.setCellValueFactory(
                new PropertyValueFactory<TableRow,String>("col1")
        );
        freqCol.setCellValueFactory(
                new PropertyValueFactory<TableRow,String>("col2")
        );

        table2.getColumns().addAll(itemCol, freqCol);

        associationPane.getChildren().add(table2);

        for (int i = 0; i < items.size(); i++) {
            String[] itemArr = {items.get(i)};
            int freq = apriori.getFrequency(apriori.transactions, itemArr);
            if (freq >= apriori.minSup){
                TableRow tableRow = new TableRow(items.get(i), String.valueOf(freq));
                data2.add(tableRow);
            }
        }

        table2.setItems(data2);

        ArrayList<String> newSet = new ArrayList<>();
        ArrayList<String> lastSet = (ArrayList) items.clone();
        
        boolean flag = false;

        do {
            
            if (flag){
                lastSet = (ArrayList)newSet.clone();
            }
            
            newSet.clear();

            for (int i = 0; i < lastSet.size(); i++) {
                apriori.getItemSet(apriori.transactions, lastSet, i, newSet);
            }

            TableView table3 = new TableView();
            ObservableList<TableRow> data3 = FXCollections.observableArrayList();

            TableColumn iCol = new TableColumn("Item set");
            TableColumn fCol = new TableColumn("count");

            iCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow,String>("col1")
            );
            fCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow,String>("col2")
            );

            table3.getColumns().addAll(iCol, fCol);



            for (int i = 0; i < newSet.size(); i++) {
                String[] itemArr = newSet.get(i).split(" ");
                int freq = apriori.getFrequency(apriori.transactions, itemArr);

                TableRow tableRow = new TableRow(newSet.get(i), String.valueOf(freq));
                data3.add(tableRow);
            }

            table3.setItems(data3);

            associationPane.getChildren().add(table3);
            flag = true;

        }while (!newSet.isEmpty());

        associationPane.getChildren().remove(associationPane.getChildren().size()-1);

        for (int i = 0; i < lastSet.size(); i++) {

            ArrayList<String> set = apriori.getSuperSet(lastSet.get(i).split(" "));

            ArrayList<String> x = new ArrayList<>();
            ArrayList<String> y = new ArrayList<>();

            apriori.getSuperSetItems(set, x, y);

            addSuperSet(apriori, x, y);
        }

        

    }

    public void addSuperSet(Apriori apriori, ArrayList<String> x, ArrayList<String> y){

        TableView table = new TableView();
        ObservableList<SuperSetRow> data = FXCollections.observableArrayList();

        TableColumn setCol = new TableColumn("set");
        TableColumn supSet = new TableColumn("sup");
        TableColumn confSet = new TableColumn("conf");

        setCol.setCellValueFactory(
                new PropertyValueFactory<SuperSetRow,String>("set")
        );
        supSet.setCellValueFactory(
                new PropertyValueFactory<SuperSetRow,String>("sup")
        );
        confSet.setCellValueFactory(
                new PropertyValueFactory<SuperSetRow,String>("conf")
        );

        table.getColumns().addAll(setCol, supSet, confSet);

        supersetPane.getChildren().add(table);

        for (int i = 0; i < x.size(); i++) {
            String[] arr1 = x.get(i).split(" ");
            String[] arr2 = y.get(i).split(" ");

            String[] merge = apriori.mergeArrays2(arr1, arr2);

            int setFreq = apriori.getFrequency(apriori.transactions, merge);
            int xFreq = apriori.getFrequency(apriori.transactions, arr1);
            int numOfTrans = apriori.transactions.size();

            double sup = (setFreq * 1.0) / numOfTrans;
            double conf = (setFreq * 1.0) / xFreq;

            int supPer = (int)Math.floor(sup * numOfTrans);
            int confPer = (int)Math.floor(conf * numOfTrans);

            System.out.println("confPer: " + confPer + "  conf:  " + apriori.minConf + "  supPer" + supPer + "  sup: " + apriori.minSup);


            if ((supPer >= apriori.minSup) && (confPer >= apriori.minConf)){
                String set = x.get(i) + " -> " + y.get(i);
                SuperSetRow superSetRow = new SuperSetRow(set, String.format("%.2f", sup), String.format("%.2f", conf));
                data.add(superSetRow);
            }
        }

        table.setItems(data);
    }

}
