// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class PedigreeQuerySteps {
    private JFrame ParentFrame;
    private DrawPedigree DrPdgr;
    private JScrollPane scroll;
    public int RqdPrsnID;
    private String FlNmSequence = "temp/Sequence.tmp";
    private String FlNmFamilies = "temp/Families.tmp";
    private String FlNmFmlGnrtn = "temp/FmlGnrtn.tmp";
    private String FlNmFmlRltns = "temp/FmlRltns.tmp";
    private String FlNmPedigree = "temp/Pedigree.tmp";
    private String FlNmPrsnsXY = "temp/PrsnsXY.tmp";
    private String FlNmLinesXY = "temp/LinesXY.tmp";

    PedigreeQuerySteps (JFrame ParentFrame, DrawPedigree DrPdgr, JScrollPane scroll) {
        this.ParentFrame = ParentFrame;
        this.DrPdgr = DrPdgr;
        this.scroll = scroll;
    }


    public boolean DoStep(int StID) {
        //Обработка списка пойнтеров
        Sequence Sqnc = new Sequence();
        try {
            if (StID == 0)
                Sqnc.DoStart(FlNmSequence, StID);
            else
                Sqnc.DoSequence(FlNmSequence, StID);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        //Отбор семей по пойнтерам
        FamiliesBySequence FmlBySqnc = new FamiliesBySequence(ParentFrame);
        try {
            FmlBySqnc.GatherFamilies(FlNmSequence, FlNmFamilies, FlNmFmlGnrtn);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        //Связи между семьями
        FamiliesRelations FmlRltns = new FamiliesRelations(FlNmFamilies, FlNmFmlGnrtn, FlNmFmlRltns);
        try {
            FmlRltns.SeekRelations(ParentFrame);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        //Рассчет поколений для семей
        FamiliesGenerations FmlsGnrtns = new FamiliesGenerations();
        if (!FmlsGnrtns.SeekGenerations(ParentFrame, FlNmFmlRltns, FlNmFmlGnrtn))
            return false;

        //Рассчет поколений для индивидов
        PersonsGenerations PrsnsGnrtns = new PersonsGenerations();
        if (!PrsnsGnrtns.SeekGenerations(ParentFrame, FlNmPedigree, FlNmFmlGnrtn, FlNmFamilies, FlNmPrsnsXY))
            return false;

        //Рассчет координат для индивидов
        Persons2d Prsns2d = new Persons2d();
        if (!Prsns2d.SeekPersonsX(ParentFrame, FlNmFmlGnrtn, FlNmFamilies, FlNmPrsnsXY))
            return false;

        //Рассчет координат линий
        PedigreeLines PdgrLns = new PedigreeLines();
        if (!PdgrLns.SeakLinesXY(ParentFrame, FlNmFmlGnrtn, FlNmFamilies, FlNmPrsnsXY, FlNmLinesXY))
            return false;

        return true;
    }


    void DoSteps () {
        if (!DoStep(0)) return;

        DrPdgr.FilePrsnXYName = FlNmPrsnsXY;
        DrPdgr.PdgrLnsFileName = FlNmLinesXY;
        DrPdgr.FilePdgrName = FlNmPedigree;
        scroll.repaint();
        DrPdgr.setSize(0,0);
        scroll.getHorizontalScrollBar().setUnitIncrement(DrPdgr.SS+DrPdgr.SD);

        DrPdgr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int i, j;
                int meX, meY;
                String sz;
                meX = me.getX();
                meY = me.getY();
                if (me.getButton() == 1) {
                    for (i=0; i<DrPdgr.getAmntPrsn(); i++) {
                        if ((meX > DrPdgr.getPrsnXY(i, 0)) & (meY > DrPdgr.getPrsnXY(i, 1)) & (meX < DrPdgr.getPrsnXY(i, 0)+DrPdgr.SS) & (meY < DrPdgr.getPrsnXY(i, 1)+DrPdgr.SS)) {
                            if (DrPdgr.getbNxt(i)) {
                                if (!DoStep(DrPdgr.getPrsnID(i))) {
                                    Sequence Sqnc = new Sequence();
                                    try {
                                        Sqnc.GetSequence(FlNmSequence);
                                    }
                                    catch (Exception e) {
                                        JOptionPane.showMessageDialog(ParentFrame, "" + e);
                                        return;
                                    }
                                    PedigreeData PdgrData = new PedigreeData();
                                    try {
                                        PdgrData.RWData(FlNmPedigree, 'r');
                                    }
                                    catch (Exception e) {
                                        JOptionPane.showMessageDialog(ParentFrame, "" + e);
                                        return;
                                    }
                                    sz = PdgrData.getPrsnID(DrPdgr.getPrsnID(i));
                                    DoStep(-1);
                                    JOptionPane.showMessageDialog(ParentFrame, "Undrawable pedigree structure!");
                                    JOptionPane.showMessageDialog(ParentFrame, "The ID of the last reqwest person is \"" + sz + "\".\nTry to run the project from this person.");
                                }
                                scroll.repaint();
                                DrPdgr.setSize(0,0);
                            }
                            return;
                        }
                    }
                }
                if (me.getButton() == 3) {
                    DoStep(-1);
                    scroll.repaint();
                    DrPdgr.setSize(0,0);
                }
            }
        });
    }
}
