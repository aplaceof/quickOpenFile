import com.intellij.ide.actions.OpenFileAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import javax.swing.JTextField ;
import javax.swing.JTextField ;
import javax.swing.JTextField ;
import javax.swing.JPanel ;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class QuickOpenForm extends JFrame {
    private static final long serialVersionUID = 1L;

    // component definition

    //  filter info input
    private JLabel infoLabel;
    private JTextField hierarchyText1;
    private JTextField hierarchyText2;
    private JTextField hierarchyText3;
    private JTextField hierarchyText4;
    private JTextField detailText1;
    private JTextField detailText2;

    private JButton confirmBtn;
    private JPanel panel1;

    //  text to filter data
    String hierarchyInfo1 = "" ;
    String hierarchyInfo2 = "" ;
    String hierarchyInfo3 = "";
    String hierarchyInfo4 = "";
    String detailInfo1 = "";
    String detailInfo2 = "";

    // JTable to display data
    private JTable pageInfoTable;
    private JPanel panel2;

    // store display data
    ArrayList<String> descList = new ArrayList<String>();
    ArrayList<String> jspNameList = new ArrayList<String>();
    ArrayList<String> flowNameList = new ArrayList<String>();

    ArrayList<String> pageList =   new ArrayList(); // origin data of file shortcut
    Project project;  // idea project
    String filePrefix = "";  // file prefix path



    // filter the data according to input text
    private void filterTableData() {
        descList.clear();
        flowNameList.clear();
        jspNameList.clear();

        // filter the data according to input text
        for( String item : pageList ) {

            String [] lineSplit = item.split(":");
            String description = lineSplit[0].trim();

            // display the data if it contains the specific text
            String [] descriptions  = description.split(",");
            String hierarchyDesc1 = descriptions[0].trim() ;
            String hierarchyDesc2 = descriptions[1].trim() ;
            String hierarchyDesc3 = descriptions[2].trim() ;
            String hierarchyDesc4 = descriptions[3].trim() ;
            String detailDesc1 = descriptions[4].trim() ;
            String detailDesc2 = descriptions[5].trim() ;

            boolean flag1 =   hierarchyInfo1.isEmpty() ||  (!hierarchyDesc1.isEmpty() &&   hierarchyDesc1.equals(hierarchyInfo1));
            boolean flag2 =   hierarchyInfo2.isEmpty() ||  (!hierarchyDesc2.isEmpty() &&   hierarchyDesc2.equals(hierarchyInfo2));
            boolean flag3 =   hierarchyInfo3.isEmpty() ||  (!hierarchyDesc3.isEmpty() &&   hierarchyDesc3.equals(hierarchyInfo3));
            boolean flag4 =   hierarchyInfo4.isEmpty() ||  (!hierarchyDesc4.isEmpty() &&   hierarchyDesc4.equals(hierarchyInfo4));
            boolean flag5 =   detailInfo1.isEmpty() ||  (!detailDesc1.isEmpty() &&   detailDesc1.equals(detailInfo1));
            boolean flag6 =   detailInfo2.isEmpty() ||  (!detailDesc2.isEmpty() &&   detailDesc2.equals(detailInfo2));


            // if it satisfies all condition
            if(  flag1 && flag2 && flag3 && flag4 && flag5 && flag6 ) {

                descList.add(description);
                String fileInfo = lineSplit[1].trim();
                String [] fileInfos = fileInfo.split(";");
                String pageFlowName =   fileInfos[0].trim();
                String jspFileName =   fileInfos[1].trim();
                flowNameList.add(pageFlowName);
                jspNameList.add(jspFileName);
            }



        }

    }
    //  set data in  the  JTable
    public void setTableData() {

        this.filterTableData();  // filter the data

        DefaultTableModel newtablemodel = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            //  set non editable
            public boolean isCellEditable(int rowIndex, int ColIndex) {
                return false;
            }
        };


        Vector v = new Vector(descList);
        newtablemodel.addColumn("Description", v);

        v = new Vector(flowNameList);
        newtablemodel.addColumn("Realted File1", v);

        v = new Vector(jspNameList);
        newtablemodel.addColumn("Realted File2", v);

        pageInfoTable.setModel(newtablemodel); // add data

        // refresh the JTable
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                pageInfoTable.updateUI();
            }
        });

    }

    // set component size
    public void setSize() {
        infoLabel.setPreferredSize(new Dimension(80,40));
        hierarchyText1.setPreferredSize(new Dimension(100,40));
        hierarchyText2.setPreferredSize(new Dimension(100,40));
        hierarchyText3.setPreferredSize(new Dimension(100,40));
        hierarchyText4.setPreferredSize(new Dimension(100,40));
        detailText1.setPreferredSize(new Dimension(100,40));
        detailText2.setPreferredSize(new Dimension(100,40));

        confirmBtn.setPreferredSize(new Dimension(80,40));
    }


    // init the JFrame
    public QuickOpenForm(ArrayList<String> pageList, Project p, String filePrefix) {

        this.pageList = pageList;
        this.project = p;
        this.filePrefix = filePrefix;

        // init the component
        panel1 = new JPanel();
        infoLabel  = new JLabel("filter info");
        hierarchyText1 = new JTextField();
        hierarchyText2 = new JTextField();
        hierarchyText3 = new JTextField();
        hierarchyText4 = new JTextField();
        detailText1 = new JTextField();
        detailText2 = new JTextField();

        confirmBtn = new JButton("confirm");

        // put into panel
        panel1.add(infoLabel);
        panel1.add(hierarchyText1);
        panel1.add(hierarchyText2);
        panel1.add(hierarchyText3);
        panel1.add(hierarchyText4);
        panel1.add(detailText1);
        panel1.add(detailText2);
        panel1.add(confirmBtn);

        this.addConfirmListener();  // add listener to  confirmBtn
        this.setSize();

        panel2 = new JPanel();
        pageInfoTable = new JTable() {
            private static final long serialVersionUID = 1L;
            //  set non editable
            public boolean isCellEditable(int rowIndex, int ColIndex) {
                return false;
            }

        };
        this.addActionListenForTable(); // add click listener
        //  set text font
        pageInfoTable.getTableHeader().setFont(new Font("Serif", Font.PLAIN, 18));
        pageInfoTable.setFont(new Font("Serif", Font.PLAIN, 15));

        // set scroll bar
        JScrollPane sp =new JScrollPane(pageInfoTable);
        sp.setPreferredSize( new Dimension(700,500 ));

        // put into panel
        panel2.setLayout(new BorderLayout());
        panel2.add(sp,BorderLayout.CENTER );
        panel2.add(sp);

        // add panel into JFrame

        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.CENTER);


        this.pack();
        // set size
        this.setSize(900, 650);
        this.setLocation(200, 200);
        this.setVisible(true);

        // set close event
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //        pageInfoTable.setGridColor(java.awt.Color.BLUE);
    }

    // add table data cell click listener
    private void addActionListenForTable() {

        pageInfoTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                //  double click to trigger
                if (e.getClickCount() == 2) {
                    // get the cell which are being clicked
                    int row = pageInfoTable.getSelectedRow();
                    int col = pageInfoTable.getSelectedColumn();
                    String filePath = (String)pageInfoTable.getValueAt(row, col);

//                    System.out.println( "open file ： "  + row + "  " + col + "  "  + filePath);
                    OpenFileAction.openFile( filePrefix + "/" + filePath, project);
                }
            }
        });
    }

    //  add  confirmBtn click listener
    private void addConfirmListener(){

        this.confirmBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hierarchyInfo1 = hierarchyText1.getText().trim();
                hierarchyInfo2 = hierarchyText2.getText().trim();
                hierarchyInfo3 = hierarchyText3.getText().trim();
                hierarchyInfo4 = hierarchyText4.getText().trim();
                detailInfo1 = detailText1.getText().trim();
                detailInfo2 = detailText2.getText().trim();

                setTableData();  // update  the data JTable display

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }
}
