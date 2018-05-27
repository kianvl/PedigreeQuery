// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

class PQMenuHandler implements ActionListener {
    private JFrame menus;
    PedigreeQuerySteps PdgrQrStps;
    private DrawPedigree DrPdgr;
    private JScrollPane scroll;
    private String ProjectName;

    PQMenuHandler (JFrame menus, DrawPedigree DrPdgr, JScrollPane scroll) {
        this.menus = menus;
        this.DrPdgr = DrPdgr;
        this.scroll = scroll;
    }

    public void actionPerformed (ActionEvent ae) {
        String arg = ae.getActionCommand();
//        boolean DataOk; // never used

        if (arg.equals("Exit")) System.exit(0);

        if (arg.equals("Save")) {
            if (ProjectName == null) {
                JOptionPane.showMessageDialog (menus, "At first you need to run the project!");
                return;
            }

            JFileChooser fc = new JFileChooser ("Projects/" + ProjectName);
            fc.showSaveDialog(menus);
            NewLinkageData LnkgData = new NewLinkageData ();
            if (fc.getSelectedFile().exists()) {
                JOptionPane.showMessageDialog(menus, "File exists!");
            }

            // запись фрагмента родословной в LINKAGE-формате
            try {
//1                LnkgData.DoNewData (fc.getSelectedFile(), "temp/Pedigree.tmp", "temp/PrsnsXY.tmp");
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(menus, "" + e);
                return;
            }

            // запись картинки фрагмента родословной
//1            PQEPSFile PQepsfile = new PQEPSFile ();
            try
            {
//1                PQepsfile.PQEPSFile (fc.getSelectedFile(), "temp/PrsnsXY.tmp", "temp/LinesXY.tmp");
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(menus, "" + e);
                return;
            }
        }

        //размеры символов и расстояний между ними
/* 2
        if (arg.equals("Sizes"))
        {
            JFrame PQSzs;
            PQSzs = new PQSizes();
            PQSzs.setSize(512, 384);
            PQSzs.repaint();
            PQSzs.setVisible(true);
            PQSzs.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent we)
                {
                    scroll.repaint();
                    DrPdgr.setSize(0,0);
                }
            });
        }
*/

        //Статистика
/* 2
        if (arg.equals("Statistics"))
        {
            JFrame PQStt;
            PQStt = new PQStatistics();
            PQStt.setSize(512, 384);
            PQStt.repaint();
            PQStt.setVisible(true);
            PQStt.addWindowListener(new WindowAdapter ()
            {
                public void windowClosing(WindowEvent we)
                {
                    scroll.repaint();
                    DrPdgr.setSize(0,0);
                }
            });
        }
*/
        //создание нового проекта
        if (arg.equals("New"))
        {
            ProjectName = (String)JOptionPane.showInputDialog(menus, "Please enter project name");
            if (ProjectName.equals("") | ProjectName==null)
                return;
            File parent = new File("Projects/" + ProjectName);
            if (parent.exists())
            {
                JOptionPane.showMessageDialog(menus, "Such project already exist!");
                return;
            }
            parent.mkdirs();
            JFrame PQPrjct;
// 3            PQPrjct = new PQProjects(ProjectName);
// 3            PQPrjct.setSize(640, 480);
// 3            PQPrjct.repaint();
// 3            PQPrjct.setVisible(true);
            menus.setTitle("PedigreeQuery - Project - " + ProjectName);
        }

        //открытие имеющегося проекта
        if (arg.equals("Open"))
        {
            ProjectName = (String)JOptionPane.showInputDialog (menus, "Please enter project name");
            if (ProjectName.equals("") | ProjectName==null)
                return;
            File parent = new File("Projects/" + ProjectName);
            if (!parent.exists())
            {
                JOptionPane.showMessageDialog (menus, "There is no such project!");
                ProjectName = null;
                return;
            }
            JOptionPane.showMessageDialog (menus, "Project \"" + ProjectName + "\" is open.");
            menus.setTitle("PedigreeQuery - Project - " + ProjectName);
            scroll.repaint();
            DrPdgr.setSize(0,0);
            File tempdir = new File ("temp");
            if (!tempdir.exists())
                tempdir.mkdirs ();
            File tempf = new File ("temp/PrsnsXY.tmp");
            if (tempf.exists())
                tempf.delete();
        }

        //редактирование имеющегося проекта
        if (arg.equals("Edit"))
        {
            if (ProjectName == null)
            {
                ProjectName = (String)JOptionPane.showInputDialog (menus, "Please enter project name");
                if (ProjectName.equals("") | ProjectName==null)
                    return;
                File parent = new File("Projects/" + ProjectName);
                if (!parent.exists())
                {
                    JOptionPane.showMessageDialog(menus, "There is no such project!");
                    ProjectName = null;
                    return;
                }
                menus.setTitle("PedigreeQuery - Project - " + ProjectName);
            }
            JFrame PQPrjct;
// 4            PQPrjct = new PQProjects(ProjectName);
// 4            PQPrjct.setSize(640, 480);
// 4            PQPrjct.repaint();
// 4            PQPrjct.setVisible(true);
        }

        //запуск проекта
        if (arg.equals("Run"))
        {
            //если проект еще не открыт, то запросить имя проекта
            if (ProjectName == null)
            {
                ProjectName = (String)JOptionPane.showInputDialog(menus, "Please enter project name");
                if (ProjectName.equals("") | ProjectName==null)
                    return;
                File parent = new File("Projects/" + ProjectName);
                if (!parent.exists())
                {
                    JOptionPane.showMessageDialog(menus, "There is no such project!");
                    ProjectName = null;
                    return;
                }
                menus.setTitle("PedigreeQuery - Project - " + ProjectName);
            }
/* 5
            PQFullDataMake FllDtMk;
            FllDtMk = new PQFullDataMake();
            try
            {
                if (!FllDtMk.MakeFullData(menus, ProjectName))
                    return;
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(menus, "" + e);
                return;
            }
*/

/* 6
            PedigreeToFamilies PdgrTFml = new PedigreeToFamilies("temp/Pedigree.tmp", "temp/Families.tmp");
            try
            {
                PdgrTFml.PedToFam(menus);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(menus, "" + e);
                return;
            }
*/

// 7            PdgrQrStps = new PedigreeQuerySteps(menus, DrPdgr, scroll);
// 7            PdgrQrStps.DoSteps();
        }
    }
}
