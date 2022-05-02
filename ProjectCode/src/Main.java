import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
//import java.util.HashMap;

public class Main extends JFrame{
    File rules_file = new File("rules.txt"); // where are rules laying
    
    int rules_count = 10; // count of rules that we have
    int size = 25; // sets the size of a square array
    int[][] startPositions = 	{{10,20},
    						{10,20-1},
						    {10,20-2},
						    {10-1,20},
						    {10-2,20-1}}; // which cells are pre-colored
    CellMachine machine = new CellMachine(size, startPositions);
    JPanel frame_panel; // root field
    JPanel main_panel; //cells field
    JPanel control_panel;
    JButton start;
    JButton refresh;
    JComboBox<String> rule; // rule choose box 
    JLabel status;
    JCheckBox old_enable; // enable coloring old cells
    JPanel[][] cells = new JPanel[size][size]; // creates cell array
    private DefaultComboBoxModel<String> rulesModel; //
    Thread update;
    
    Color[] colorList = {
            new Color(0x000000), // black
            new Color(0xFFFFFF), // white
            new Color(0xAA0000)  // red 
    };
    final Color fgColorNew = colorList[0];
    final Color fgColorOld = colorList[2];
    final Color bgColor = colorList[1];
    int gap=0; // net size
    public boolean flagExecuted = false; // is running stopped
    boolean old_enabled=false; // is coloring old cells enabled
    Runnable update_field = () ->{
        while(flagExecuted){
            machine.update_thread();
            for(int y = 0; y < size; y++){
                for (int x = 0; x < size; x++) {
                    //check color changes
                	Color cellColor = cells[x][y].getBackground();
                	int cellVal = machine.cells[x][y];
                    
                	if (cellVal != ((cellColor == bgColor) ? 0 : 1)){
                        cells[x][y].setBackground((cellVal == 1) ? fgColorNew : bgColor);
                    } else if (cellColor == fgColorNew && old_enabled){
                        cells[x][y].setBackground(fgColorOld);
                    }
                }
            }
            
            main_panel.revalidate();
            try {
                Thread.currentThread().sleep(500);
            }
            catch (Exception e){}
        }
    };
    public Main(){
        main_panel 		= new JPanel();
        frame_panel 	= new JPanel();
        control_panel 	= new JPanel();

        //creating cells and adding them to main panel
        for(int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                cells[x][y] = new JPanel();
                main_panel.add(cells[x][y]);
                cells[x][y].setBackground(bgColor);
            }
        }
        //configuring GUI
        setContentPane(frame_panel);
        frame_panel.add(main_panel);
        frame_panel.add(control_panel);
        control_panel.setLayout(new GridLayout(15,1,3,3));
        main_panel.setLayout(new GridLayout(size, size, gap, gap));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Cell Machine");
        //enable GUI functionality
        init_start();
        init_refresh();
        try{init_rule();}
        catch(Exception e){}
        
        init_old_enable();
        init_status();
        //finish configuring GUI
        pack();
        setVisible(true);
        setAlwaysOnTop(true);
    }
    
    public static void main(String[] args) {
        new Main();
    }

    public void init_rule() throws FileNotFoundException{
        Scanner rules_scanner = new Scanner(rules_file);
        //try {
            
        //} 
        //catch (FileNotFoundException e){System.out.println(123235);}
        
        rulesModel = new DefaultComboBoxModel<String>();

        while(rules_scanner.hasNext()){
        	String currrow = rules_scanner.nextLine();  
            rulesModel.addElement(currrow);
        }

        rule = new JComboBox<String>(rulesModel);
        rule.setToolTipText("Правила");
        control_panel.add(rule);
        rules_scanner.close();
    }
    
    public void init_start(){
        start = new JButton("Start");
        control_panel.add(start);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    status.setText("Status: running.");
                } catch(Exception ex){}
                start.setEnabled(false);
                old_enable.setEnabled(false);

                machine.parseRule(String.valueOf(rule.getSelectedItem()));
                flagExecuted = true;
                update = new Thread(update_field);
                update.start();
            }
        });
    }
    
    public void init_refresh(){
        refresh = new JButton("Refresh");
        control_panel.add(refresh);
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                start.setEnabled(true);
                old_enable.setEnabled(true);
                status.setText("Status: paused.");
                machine.clearField();
                machine.setupField(startPositions);
                flagExecuted = false;
            }
        });
    }
    
    public void init_old_enable(){
        old_enable = new JCheckBox("Color old");
        control_panel.add(old_enable);
        old_enable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                old_enabled = !old_enabled;
            }
        });
    }
    
    public void init_status(){
        status = new JLabel();
        control_panel.add(status);
    }
}
