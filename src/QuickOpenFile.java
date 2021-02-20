import com.intellij.ide.actions.OpenFileAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuickOpenFile extends AnAction {

    String userHome = System.getProperty("user.home");   //user home
    String quickFindFilePath =  userHome + "/IDEAConfig/quickFind.txt";
    String filePrefixPath =  userHome + "/IDEAConfig/filePrefix.txt";

    ArrayList<String> pageList =   new ArrayList();
    String filePrefix = "";  // file prefix path

    // read the config
    private void readConfig(Project p){
        pageList.clear();
        readFileByLines(quickFindFilePath, p);
        readFilePrefix(filePrefixPath, p);
    }

    /**
     * @Description: add all file shortcut to  list
     * @Param line
     * @Param p idea project  (for debug )
     * @Return: void
     * @Exception:
     * @author: alf
     * @date: 2021/2/19
     */
    private void addToList(String line, Project p ) {
        this.pageList.add(line);
    }

    public void readFileByLines(String fileName, Project p ) {

        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // read oneline per iteration
            while ((tempString = reader.readLine()) != null) {
                if( !tempString.trim().isEmpty())  {
                    this.addToList(tempString,p);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    /**
     * @Description: read file prefix path
     * @Param fileName the file which stores the configration
     * @Param p the project
     * @Return: void
     * @Exception:
     * @author: alf
     * @date: 2021/2/19
     */
    public void readFilePrefix(String fileName, Project p ) {

        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            filePrefix = reader.readLine().trim();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /***
     * @Description:  display file shortcut menu when  enter the key
     * @Param e
     * @Return: void
     * @Exception:
     * @author: aplaceof
     * @date: 2021/2/19
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project p  = e.getData(PlatformDataKeys.PROJECT);
        this.readConfig(p); //  load  plugins config first

//        String title  =  "" + pageList.size();
//        Messages.showMessageDialog(p, title, title,  Messages.getInformationIcon());

        QuickOpenForm tf = new QuickOpenForm(pageList, p, filePrefix);
        tf.setTableData();

    }

}
