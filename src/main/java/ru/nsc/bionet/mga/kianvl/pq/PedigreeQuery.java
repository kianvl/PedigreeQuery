// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PedigreeQuery extends JFrame {
    public PedigreeQuery() {
        super("PedigreeQuery");
        DrawPedigree DrPdgr = new DrawPedigree(this, null, null);
        JScrollPane scroll = new JScrollPane(DrPdgr);
        getContentPane().add (scroll, BorderLayout.CENTER);

        PQMenuHandler handler = new PQMenuHandler (this, DrPdgr, scroll);

        JMenuBar MainMenuBar = new JMenuBar();
        setJMenuBar (MainMenuBar);

        JMenu Menu_File = new JMenu ("File");
        JMenuItem MenuItem_File_Save, MenuItem_File_Exit;
        Menu_File.add(MenuItem_File_Save = new JMenuItem ("Save"));
        Menu_File.addSeparator();
        Menu_File.add (MenuItem_File_Exit = new JMenuItem ("Exit"));
        MainMenuBar.add (Menu_File);

        JMenu Menu_Project = new JMenu ("Project");
        JMenuItem MenuItem_Project_New, MenuItem_Project_Open, MenuItem_Project_Edit, MenuItem_Project_Run;
        Menu_Project.add (MenuItem_Project_New = new JMenuItem ("New"));
        Menu_Project.add (MenuItem_Project_Open = new JMenuItem ("Open"));
        Menu_Project.add (MenuItem_Project_Edit = new JMenuItem ("Edit"));
        Menu_Project.add (MenuItem_Project_Run = new JMenuItem ("Run"));
        MainMenuBar.add (Menu_Project);

        JMenu Menu_Options = new JMenu ("Options");
        JMenuItem MenuItem_Options_Sizes, MenuItem_Options_Statis;
        Menu_Options.add (MenuItem_Options_Sizes = new JMenuItem ("Sizes"));
        Menu_Options.add (MenuItem_Options_Statis = new JMenuItem ("Statistics"));
        MainMenuBar.add (Menu_Options);

//		JMenu Menu_Help = new JMenu("Help");
//		JMenuItem MenuItem_Help_Index, MenuItem_Help_About;
//		Menu_Help.add(MenuItem_Help_Index = new JMenuItem("Index"));
//		Menu_Help.addSeparator();
//		Menu_Help.add(MenuItem_Help_About = new JMenuItem("About"));
//		MainMenuBar.add(Menu_Help);

        MenuItem_File_Exit.addActionListener (handler);
        MenuItem_File_Save.addActionListener (handler);
        MenuItem_Options_Sizes.addActionListener (handler);
        MenuItem_Options_Statis.addActionListener (handler);
        MenuItem_Project_New.addActionListener (handler);
        MenuItem_Project_Open.addActionListener (handler);
        MenuItem_Project_Edit.addActionListener (handler);
        MenuItem_Project_Run.addActionListener (handler);
    }


    public Dimension getPreferredSize() {
        Dimension PQDmnsn;
        return PQDmnsn = new Dimension(512, 384);
    }


    public static void main (String args[]) {
        PedigreeQuery app = new PedigreeQuery();
        WindowListener wL = new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                ((Window) e.getSource()).dispose();
                System.exit(0);
            }
        };
        app.addWindowListener (wL);
        app.setTitle ("PedigreeQuery");
        app.pack();
        app.setVisible (true);
    }
}
