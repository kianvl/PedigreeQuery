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

    // Инициализация EPS-файла.
    // NameEPSFile - название EPS-файла
    // X - размер картинки по горизонтали
    // Y - размер картинки по вертикали
    void EPSFileInitilize (String NameEPSFile, int X, int Y) throws Exception {
        EPSFile = new FileWriter (NameEPSFile + ".eps");
//		EPSFile.write ("%");
        EPSFile.write ("%%!PS-Adobe-3.0 EPSF-3.0\n");
        EPSFile.write ("%%BoundingBox: 0 0 " + X + " " + Y + "\n");
        EPSFile.write ("/$F2psDict 200 dict def\n");
        EPSFile.write ("$F2psDict begin\n");
        EPSFile.write ("$F2psDict\n");
        EPSFile.write ("/mtrx matrix put\n");
    }

    // Создание цветовой гаммы.
    // Cl - номер цвета. Задается пользователем.
    void EPSFileColors (int Cl, int R, int G, int B) throws Exception {
        EPSFile.write ("/col" + Cl + " {" + (double)R/255.0 + " " + (double)G/255.0 + " " + (double)B/255.0 + " srgb} bind def\n");
    }

    // Создание основных объектов. Минимальный набор.
    void EPSFileObjects () throws Exception {
        EPSFile.write ("/cp {closepath} bind def\n");
        EPSFile.write ("/ef {eofill} bind def\n");
        EPSFile.write ("/gr {grestore} bind def\n");
        EPSFile.write ("/gs {gsave} bind def\n");
        EPSFile.write ("/l {lineto} bind def\n");
        EPSFile.write ("/m {moveto} bind def\n");
        EPSFile.write ("/n {newpath} bind def\n");
        EPSFile.write ("/s {stroke} bind def\n");
        EPSFile.write ("/slw {setlinewidth} bind def\n");
        EPSFile.write ("/srgb {setrgbcolor} bind def\n");
        EPSFile.write ("/rot {rotate} bind def\n");
        EPSFile.write ("/sc {scale} bind def\n");
        EPSFile.write ("/tr {translate} bind def\n\n");
        EPSFile.write ("/DrawCircle {\n");
        EPSFile.write ("    /endangle exch def\n");
        EPSFile.write ("    /startangle exch def\n");
        EPSFile.write ("    /yrad exch def\n");
        EPSFile.write ("    /xrad exch def\n");
        EPSFile.write ("    /y exch def\n");
        EPSFile.write ("    /x exch def\n");
        EPSFile.write ("    /savematrix mtrx currentmatrix def\n");
        EPSFile.write ("    x y tr xrad yrad sc 0 0 1 startangle endangle arc\n");
        EPSFile.write ("    closepath\n");
        EPSFile.write ("    savematrix setmatrix\n");
        EPSFile.write ("    } def\n\n");
        EPSFile.write ("/DrawRing {\n");
        EPSFile.write ("    /endangle exch def\n");
        EPSFile.write ("    /startangle exch def\n");
        EPSFile.write ("    /yrad exch def\n");
        EPSFile.write ("    /xrad exch def\n");
        EPSFile.write ("    /y exch def\n");
        EPSFile.write ("    /x exch def\n");
        EPSFile.write ("    /savematrix mtrx currentmatrix def\n");
        EPSFile.write ("    x y tr xrad yrad sc 0 0 1 startangle endangle arc\n");
        EPSFile.write ("    savematrix setmatrix\n");
        EPSFile.write ("    } def\n\n");
        EPSFile.write ("/ff {findfont} bind def\n");
        EPSFile.write ("/scf {scalefont} bind def\n");
        EPSFile.write ("/sf {setfont} bind def\n");
        EPSFile.write ("/sh {show} bind def\n\n");
        EPSFile.write ("/gr {grestore} bind def\n");
        EPSFile.write ("/$F2psBegin {$F2psDict begin /$F2psEnteredState save def} def\n");
        EPSFile.write ("/$F2psEnd {$F2psEnteredState restore end} def\n\n");
        EPSFile.write ("$F2psBegin\n");
    }

    // Задание толщины линии
    void EPSLineWidth (double Width) throws Exception {
        EPSFile.write (Width + " slw\n");
    }

    // Задание точки начала линии.
    // Y - координата по вертикали, X - координата по горизонтали.
    void EPSLineStart (int X, int Y) throws Exception {
        EPSFile.write ("n " + X + " " + Y + " m ");
    }

    // Продолжение линии.
    // Y - координата по вертикали, X - координата по горизонтали.
    void EPSLineTo (int X, int Y) throws Exception {
        EPSFile.write (X + " " + Y + " l ");
    }

    // Окончание линии.
    // Cl - номер цвета.
    // CP - замкнутая линия.
    // EF - заполнение фигуры.
    void EPSLineEnd (int Cl, int CP, int EF) throws Exception {
        if (CP == 1)
            EPSFile.write ("cp ");
        EPSFile.write ("gs col" + Cl + " ");
        if (EF == 1)
            EPSFile.write ("ef ");
        EPSFile.write ("s gr\n");
    }

    // Дуга, окружность, сегмент, круг.
    // RC - замкнутая фигура (сегмент), незамкнутая фигура (дуга).
    // X, Y - координаты центра.
    // W, H - ширина и высота окружности.
    // S, E - углы начала и конца дуги.
    // EF - заполнение фигуры цветом Cl.
    void EPSFileArc (int RC, int X, int Y, int W, int H, int S, int E, int EF, int Cl) throws Exception {
        EPSFile.write ("n " + X + " " + Y + " " + W + " " + H + " " + S + " " + E + " ");
        if (RC == 0)
            EPSFile.write ("DrawRing ");
        if (RC == 1)
            EPSFile.write ("DrawCircle ");
        EPSFile.write ("gs col" + Cl + " ");
        if (EF == 1)
            EPSFile.write ("ef ");
        EPSFile.write ("s gr\n");
    }

    // Текст.
    // Sz - размер шрифта.
    // X, Y - координаты текста.
    // W, H - коэффициенты ширины и высоты текста.
    // R - поворот текста.
    // S - текст.
    // Cl - цвет текста.
    void EPSFileText (int Sz, int X, int Y, int W, int H, int R, String S,  int Cl) throws Exception {
        EPSFile.write ("/Times-Roman ff " + Sz + " scf sf " + X + " " + Y + " m gs " + W + " " + H + " sc " + R + " rot (" + S + ") col" + Cl + " sh gr\n");
//        EPSFile.write ("/SansSerif ff " + Sz + " scf sf " + X + " " + Y + " m gs " + W + " " + H + " sc " + R + " rot (" + S + ") col" + Cl + " sh gr\n");
    }

    // Закрытие файла.
    void EPSFileClose () throws Exception {
        EPSFile.write ("$F2psEnd\n");
        EPSFile.close();
    }
}
