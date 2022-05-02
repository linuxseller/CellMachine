import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class Main extends JFrame{
    File rules_file = new File("rules.txt");
    Scanner rules_scanner;
    int rules_count=10;
    CellMashine machine = new CellMashine();
    JPanel frame_panel; // хранилище данных окна
    JPanel main_panel; //основное хранилище клеток
    JPanel control_panel;
    JButton start;
    JButton refresh;
    JComboBox rule;
    JComboBox new_color_choose;
    JComboBox old_color_choose;
    JLabel status;
    JCheckBox old_enable;
    int size=machine.SIZE; // задаёт размер квадратного массива
    JPanel[][] cells = new JPanel[size][size]; // создание массива клеток
    private DefaultComboBoxModel<String> rulesModel;
    private DefaultComboBoxModel<Color> colorModel;
    Thread update;
    //HashMap<String, Color> colorList = new HashMap<>();
    //colorList.put("Black");
    
    Color[] colorList = {
            new Color(0x000000),
            new Color(0xFFFFFF),
            new Color(0xAA0000)
    };
    final Color fgColorNew = colorList[0];
    final Color fgColorOld = colorList[2];
    final Color bgColor = colorList[1];
    int gap=0;
    public String[] elements = new String[rules_count];
    public boolean flagExecuted = false;
    boolean old_enabled=false;
    Runnable update_field = () ->{
        while(flagExecuted){
            machine.update_thread();
            for(int y = 0; y < size; y++){
                for (int x = 0; x < size; x++) {
                    //изменился ли цвет
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
        setContentPane(frame_panel);
        frame_panel.add(main_panel);
        frame_panel.add(control_panel);
        control_panel.setLayout(new GridLayout(15,1,3,3));
        main_panel.setLayout(new GridLayout(size, size, gap, gap));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Cell Machine");
        //включаем функции
        validate_start();
        validate_refresh();
        validate_rule();
        validate_old_enable();
        validate_status();
        //validate_color();
        pack();
        setVisible(true);
        setAlwaysOnTop(true);
    }
    
    public static void main(String[] args) {
        new Main();
    }

    public void validate_rule(){
        try {
            rules_scanner = new Scanner(rules_file);
        } 
        catch (FileNotFoundException e){}
        
        rulesModel = new DefaultComboBoxModel<String>();

        for(int i = 0; rules_scanner.hasNext(); i++){
        	String currrow = rules_scanner.nextLine();  
            elements[i] = currrow;
            rulesModel.addElement(currrow);
        }

        rule = new JComboBox<String>(rulesModel);
        rule.setToolTipText("Правила");
        control_panel.add(rule);
    }
    
    public void validate_start(){
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

    public void validate_color(){
        colorModel = new DefaultComboBoxModel<Color>();
        for (int i = 0; i < colorList.length; i++){
            colorModel.addElement(colorList[i]);
        }
        new_color_choose = new JComboBox(colorModel);
        old_color_choose = new JComboBox(colorModel);
        control_panel.add(new_color_choose);
        control_panel.add(old_color_choose);
    }
    
    public void validate_refresh(){
        refresh = new JButton("Refresh");
        control_panel.add(refresh);
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                start.setEnabled(true);
                old_enable.setEnabled(true);
                status.setText("Status: paused.");
                machine.clearField();
                machine.setupField();
                flagExecuted = false;
                //******************
                //
                //stop update thread
                //******************
            }
        });
    }
    
    public void validate_old_enable(){
        old_enable = new JCheckBox("Color old");
        control_panel.add(old_enable);
        old_enable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                old_enabled = !old_enabled;
            }
        });
    }
    
    public void validate_status(){
        status = new JLabel();
        control_panel.add(status);
    }
}