// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.FileWriter;

// Класс для создания графического файла в формате EPS векторной графики.
// Свойства:
// 1) Минимальный набор графических объектов;
// 2) Одностраничность.
class WriteEPSFile  {
    private FileWriter EPSFile;
    private String s;

    // Инициализация EPS-файла.
    // NameEPSFile - название EPS-файла
    // X - размер картинки по горизонтали
    // Y - размер картинки по вертикали
    void EPSFileInitilize (String NameEPSFile, int X, int Y) throws Exception {
        NameEPSFile = "" + NameEPSFile + ".eps";
        EPSFile = new FileWriter (NameEPSFile);
//		s = "%";
//		EPSFile.write (s);
        s = "%%!PS-Adobe-3.0 EPSF-3.0\n";
        EPSFile.write (s);
        s = "%%BoundingBox: 0 0 " + X + " " + Y + "\n";
        EPSFile.write (s);
        s = "/$F2psDict 200 dict def\n";
        EPSFile.write (s);
        s = "$F2psDict begin\n";
        EPSFile.write (s);
        s = "$F2psDict\n";
        EPSFile.write (s);
        s = "/mtrx matrix put\n";
        EPSFile.write (s);
    }

    // Создание цветовой гаммы.
    // Cl - номер цвета. Задается пользователем.
    void EPSFileColors (int Cl, int R, int G, int B) throws Exception {
        s = "/col" + Cl + " {" + (double)R/255.0 + " " + (double)G/255.0 + " " + (double)B/255.0 + " srgb} bind def\n";
        EPSFile.write (s);
    }

    // Создание основных объектов. Минимальный набор.
    void EPSFileObjects () throws Exception {
        s = "/cp {closepath} bind def\n";
        EPSFile.write (s);
        s = "/ef {eofill} bind def\n";
        EPSFile.write (s);
        s = "/gr {grestore} bind def\n";
        EPSFile.write (s);
        s = "/gs {gsave} bind def\n";
        EPSFile.write (s);
        s = "/l {lineto} bind def\n";
        EPSFile.write (s);
        s = "/m {moveto} bind def\n";
        EPSFile.write (s);
        s = "/n {newpath} bind def\n";
        EPSFile.write (s);
        s = "/s {stroke} bind def\n";
        EPSFile.write (s);
        s = "/slw {setlinewidth} bind def\n";
        EPSFile.write (s);
        s = "/srgb {setrgbcolor} bind def\n";
        EPSFile.write (s);
        s = "/rot {rotate} bind def\n";
        EPSFile.write (s);
        s = "/sc {scale} bind def\n";
        EPSFile.write (s);
        s = "/tr {translate} bind def\n\n";
        EPSFile.write (s);
        s = "/DrawCircle {\n";
        EPSFile.write (s);
        s = "    /endangle exch def\n";
        EPSFile.write (s);
        s = "    /startangle exch def\n";
        EPSFile.write (s);
        s = "    /yrad exch def\n";
        EPSFile.write (s);
        s = "    /xrad exch def\n";
        EPSFile.write (s);
        s = "    /y exch def\n";
        EPSFile.write (s);
        s = "    /x exch def\n";
        EPSFile.write (s);
        s = "    /savematrix mtrx currentmatrix def\n";
        EPSFile.write (s);
        s = "    x y tr xrad yrad sc 0 0 1 startangle endangle arc\n";
        EPSFile.write (s);
        s = "    closepath\n";
        EPSFile.write (s);
        s = "    savematrix setmatrix\n";
        EPSFile.write (s);
        s = "    } def\n\n";
        EPSFile.write (s);
        s = "/DrawRing {\n";
        EPSFile.write (s);
        s = "    /endangle exch def\n";
        EPSFile.write (s);
        s = "    /startangle exch def\n";
        EPSFile.write (s);
        s = "    /yrad exch def\n";
        EPSFile.write (s);
        s = "    /xrad exch def\n";
        EPSFile.write (s);
        s = "    /y exch def\n";
        EPSFile.write (s);
        s = "    /x exch def\n";
        EPSFile.write (s);
        s = "    /savematrix mtrx currentmatrix def\n";
        EPSFile.write (s);
        s = "    x y tr xrad yrad sc 0 0 1 startangle endangle arc\n";
        EPSFile.write (s);
        s = "    savematrix setmatrix\n";
        EPSFile.write (s);
        s = "    } def\n\n";
        EPSFile.write (s);
        s = "/ff {findfont} bind def\n";
        EPSFile.write (s);
        s = "/scf {scalefont} bind def\n";
        EPSFile.write (s);
        s = "/sf {setfont} bind def\n";
        EPSFile.write (s);
        s = "/sh {show} bind def\n\n";
        EPSFile.write (s);
        s = "/gr {grestore} bind def\n";
        EPSFile.write (s);
        s = "/$F2psBegin {$F2psDict begin /$F2psEnteredState save def} def\n";
        EPSFile.write (s);
        s = "/$F2psEnd {$F2psEnteredState restore end} def\n\n";
        EPSFile.write (s);
        s = "$F2psBegin\n";
        EPSFile.write (s);
    }

    // Задание толщины линии
    void EPSLineWidth (double Width) throws Exception {
        s = "" + Width + " slw\n";
        EPSFile.write (s);
    }

    // Задание точки начала линии.
    // Y - координата по вертикали, X - координата по горизонтали.
    void EPSLineStart (int X, int Y) throws Exception {
        s = "n " + X + " " + Y + " m ";
        EPSFile.write (s);
    }

    // Продолжение линии.
    // Y - координата по вертикали, X - координата по горизонтали.
    void EPSLineTo (int X, int Y) throws Exception {
        s = "" + X + " " + Y + " l ";
        EPSFile.write (s);
    }

    // Окончание линии.
    // Cl - номер цвета.
    // CP - замкнутая линия.
    // EF - заполнение фигуры.
    void EPSLineEnd (int Cl, int CP, int EF) throws Exception {
        if (CP == 1) {
            s = "cp ";
            EPSFile.write (s);
        }
        s = "gs col" + Cl + " ";
        EPSFile.write (s);
        if (EF == 1) {
            s = "ef ";
            EPSFile.write (s);
        }
        s = "s gr\n";
        EPSFile.write (s);
    }

    // Дуга, окружность, сегмент, круг.
    // RC - замкнутая фигура (сегмент), незамкнутая фигура (дуга).
    // X, Y - координаты центра.
    // W, H - ширина и высота окружности.
    // S, E - углы начала и конца дуги.
    // EF - заполнение фигуры цветом Cl.
    void EPSFileArc (int RC, int X, int Y, int W, int H, int S, int E, int EF, int Cl)  throws Exception {
        s = "n " + X + " " + Y + " " + W + " " + H + " " + S + " " + E + " ";
        EPSFile.write (s);
        if (RC == 0) {
            s = "DrawRing ";
            EPSFile.write (s);
        }
        if (RC == 1) {
            s = "DrawCircle ";
            EPSFile.write (s);
        }
        s = "gs col" + Cl + " ";
        EPSFile.write (s);
        if (EF == 1) {
            s = "ef ";
            EPSFile.write (s);
        }
        s = "s gr\n";
        EPSFile.write (s);
    }

    // Текст.
    // Sz - размер шрифта.
    // X, Y - координаты текста.
    // W, H - коэффициенты ширины и высоты текста.
    // R - поворот текста.
    // S - текст.
    // Cl - цвет текста.
    void EPSFileText (int Sz, int X, int Y, int W, int H, int R, String S,  int Cl) throws Exception {
        s = "/Times-Roman ff " + Sz + " scf sf " + X + " " + Y + " m gs " + W + " " + H + " sc " + R + " rot (" + S + ") col" + Cl + " sh gr\n";
//		s = "/SansSerif ff " + Sz + " scf sf " + X + " " + Y + " m gs " + W + " " + H + " sc " + R + " rot (" + S + ") col" + Cl + " sh gr\n";
        EPSFile.write (s);
    }

    // Закрытие файла.
    void EPSFileClose () throws Exception {
        String s = "$F2psEnd\n";
        EPSFile.write (s);
        EPSFile.close();
    }
}
