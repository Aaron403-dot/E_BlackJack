
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Random;
import java.util.Scanner;

public class App {

    /*Declaracion de las variables iniciales dentro del main + la llamada al RUN del juego */
    public static void main(String[] args) throws Exception {
        
        String[] Cartas = new String[52];
        Scanner sc = new Scanner(System.in);
        App src = new App();
        String[] TuMano = new String[12];
        String[] Crupier = new String[12];
        Cartas = src.GeneraraMazo(Cartas);
        Cartas = src.MezclarMazo(Cartas);
        src.Jugar(Cartas, Crupier, TuMano, sc);

        sc.close();
    }

    /* Este metodo se ocupara de crear el mazo los palos se representaran como -* (El asterisko reprecentando la primera letra de la carta)*/
    private String[] GeneraraMazo(String[] Cartas){
        String[] Corazon = {"A-C", "2-C", "3-C", "4-C", "5-C", "6-C", "7-C", "8-C", "9-C", "10-C", "J-C", "Q-C", "K-C"};
        String[] Pica = {"A-P", "2-P", "3-P", "4-P", "5-P", "6-P", "7-P", "8-P", "9-P", "10-P", "J-P", "Q-P", "K-P"};
        String[] Trebol = {"A-T", "2-T", "3-T", "4-T", "5-T", "6-T", "7-T", "8-T", "9-T", "10-T", "J-T", "Q-T", "K-T"};
        String[] Diamante = {"A-D", "2-D", "3-D", "4-D", "5-D", "6-D", "7-D", "8-D", "9-D", "10-D", "J-D", "Q-D", "K-D"};
        int index=0;
        for (String s : Corazon) {
            Cartas[index++] = s;
        }
        for (String s : Pica) {
            Cartas[index++] = s;
        }
        for (String s : Trebol) {
            Cartas[index++] = s;
        }
        for (String s : Diamante) {
            Cartas[index++] = s;
        }

        return Cartas;
    }
    /*Se ocupa de mesclar el mazo con el algoritmo Fisher-Yates */
    private String[] MezclarMazo(String[] Cartas)
    {
        Random r = new Random();
        String Temp;
        int azar;
        for (int i = 51; i > 1; i--) {
            azar = r.nextInt(1, i);
            Temp = Cartas[azar];
            Cartas[azar] = Cartas[i];
            Cartas[i] = Temp;
        }
        return Cartas;
    }

    /*funcion RUN del juego de blackjack 21 */

    private void Jugar(String[] Cartas,String[] crupier,String[] TuMano,Scanner sc)
    {
        try {
            boolean gameState = true;
            boolean TurnoJ = true;
            boolean TurnoC = true;
            int limiteCartas = 51;
            int PuntuacionJugador;
            int PuntuacionCrupier;
            int[] ContenedorDeCartasYBaraja/*  = new int[2]*/;
            while (gameState) {
                ManoReset(crupier);
                ManoReset(TuMano);
                for (int i = 0; i < entregaCartas(Cartas, limiteCartas).length; i++) {
                    crupier[i] =entregaCartas(Cartas, limiteCartas)[i];
                    limiteCartas--;
                }

                System.out.println("La primera carta del Crupier es " + crupier[0]);
                
                for (int i = 0; i < entregaCartas(Cartas, limiteCartas).length; i++) {
                    TuMano[i] =entregaCartas(Cartas, limiteCartas)[i];
                    limiteCartas--;
                }

                ContenedorDeCartasYBaraja = JugadorTurno(limiteCartas, Cartas, TuMano, TurnoJ, sc);
                PuntuacionJugador = ContenedorDeCartasYBaraja[0];
                limiteCartas = ContenedorDeCartasYBaraja[1];
                if (PuntuacionJugador==-1) {
                    System.out.println("Has perdido mas suerte a la proxima");
                    return;
                }
                ContenedorDeCartasYBaraja = CrupierTurno(limiteCartas, Cartas, crupier, TurnoC);
                PuntuacionCrupier = ContenedorDeCartasYBaraja[0];
                limiteCartas = ContenedorDeCartasYBaraja[1];
                if (PuntuacionCrupier==-1) {
                    System.out.println("El Crupier se a pasado");
                    System.out.println("Has Ganado");
                    return;
                }
                
                if (PuntuacionCrupier<PuntuacionJugador) {
                    gameState=false;
                    System.err.println("Has Ganado");
                }
                else if (PuntuacionCrupier>PuntuacionJugador) {
                    gameState=false;
                    System.err.println("El crupier tiene el numero mas alto has perdido");
                }
                if (PuntuacionCrupier==PuntuacionJugador) {
                    System.out.println("Igualados a empezar otra vez");
                }

            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
    }

    /* Metodo dedicado a la entrega de las 2 cartas del incio */
    private String[] entregaCartas(String[] Cartas,int LimiteCartas){
        try{
        String[] CartasAEntregar = new String[2];
        for (int i = 0; i < CartasAEntregar.length; i++) {
            CartasAEntregar[i] = Cartas[LimiteCartas-i];
        }
        return CartasAEntregar;
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
            return Cartas;
        }
    }


    /*Muestra la mano */

    private void MostrarMano(String[] Mano)
    {
        System.out.println("Estas son las cartas: ");
        for (int i = 0; i < Mano.length; i++) {
            if (Mano[i]!=null) {
               System.out.print(" [" + Mano[i] + "] ");
            }
        }
    }


    /*Metodo de reseteo de la mano y las cartas */
    private String[] ManoReset(String[] Mano)
    {
        if (Mano[2] != null) {
            for (int i = 0; i < Mano.length; i++) {
                Mano[i] = null;
            }
        }
        return Mano;
    }


    /* Instancia del turno del jugador se realizaran todas las funciones del jugador */

    private int[] JugadorTurno(int LimiteCartas,String[] Cartas, String[] TuMano,boolean TurnoJ,Scanner sc)
    {
        try {
            int CartasEnMano = 2;
            int puntuacion;
            int[] PuntuacionYCartasPedidads = {0,LimiteCartas};
            while (TurnoJ) {
                System.out.println("Es su turno");
                MostrarMano(TuMano);
                puntuacion = converte(TuMano);
                System.out.println("");
                System.out.println("Tu puntuacion es: " + puntuacion);

                if (Check21(TuMano)) {
                    System.out.println("\n Tienes 21 \n ");
                    PuntuacionYCartasPedidads[0] = 21;
                    return PuntuacionYCartasPedidads;
                }

                System.out.println("Que desea hacer");
                System.out.println("[H]it o [F]old");
                switch (sc.nextLine().toUpperCase()) {
                    case "H" -> {
                        TuMano[CartasEnMano] = Hit(Cartas, LimiteCartas);
                        LimiteCartas--;
                        CartasEnMano++;
                        PuntuacionYCartasPedidads[1] = LimiteCartas;
                        if (converte(TuMano)>21) {
                            MostrarMano(TuMano);
                            System.out.println("\n Oops mas de 21 \n");
                            PuntuacionYCartasPedidads[0] = -1;
                            return PuntuacionYCartasPedidads;
                        }
                    }

                    case "F" -> {
                        TurnoJ = false;
                        System.out.println("Tu puntuacion final es de: " + puntuacion);
                        PuntuacionYCartasPedidads[0] = puntuacion;
                    }
                    default -> System.err.println("Escriba una opcion valida");
                }
            
        }
        return PuntuacionYCartasPedidads;
    }
    catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    /*Comprobador de que la mano es = a 21 */

    private boolean Check21(String[] Mano)
    {
        return(converte(Mano)==21);
    }


    /*Metodo de turno del crupier con la IA mas basica de la historia */

    private int[] CrupierTurno(int LimiteCartas,String[] Cartas, String[] crupier,boolean TurnoC)
    {
        try {
            int CartasEnMano = 2;
            int puntuacion;
            int[] PuntuacionYCartasPedidads = {0,LimiteCartas};
            int SimpleAI;
            while (TurnoC) {
                System.out.println("es el turno del crupier");
                MostrarMano(crupier);
                System.out.print("\n");
                puntuacion = converte(crupier);
                
                if (Check21(crupier)) {
                    System.out.println("\n El crupier Tiene 21 \n ");
                    PuntuacionYCartasPedidads[0] = 21;
                    return PuntuacionYCartasPedidads;
                }

                if (puntuacion<17) {
                    SimpleAI = 1;
                }
                else{
                    SimpleAI = 2;
                }
                
                switch (SimpleAI) {
                    case 1 -> {
                        crupier[CartasEnMano] = Hit(Cartas, LimiteCartas);
                        LimiteCartas--;
                        CartasEnMano++;
                        PuntuacionYCartasPedidads[1] = LimiteCartas;
                        if (converte(crupier)>21) {
                            MostrarMano(crupier);
                            System.out.println("\n Oops mas de 21 \n");
                            PuntuacionYCartasPedidads[0] = -1;
                            return PuntuacionYCartasPedidads;
                        }
                    }

                    case 2 -> {
                        MostrarMano(crupier);
                        TurnoC = false;
                        System.out.println("La puntuacion final del crupier es de: " + puntuacion);
                        PuntuacionYCartasPedidads[0] = puntuacion;
                    }
                    default -> {
                    }
                }
                
        }
        return PuntuacionYCartasPedidads;
    }
    catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /*Metodo de Hit para pedir cartas */

    private String Hit(String[] Cartas,int LimiteCartas){
        try{
            String CartasAEntregar = Cartas[LimiteCartas-1];
            return CartasAEntregar;
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /*Convierte el String de numero en el numero que debe ser ademas de que convierte las J Q y K en 10 ademas de asignar el valor al As */

    private int converte(String[] Mano){
    
        int Converted=0;

        String SToInt;
        
        int counter = 0;
        
        String[] ContenedorAMedida;
        
        for(int i = 0; i < Mano.length; i++) {
        
            if (Mano[i]!= null) {
            counter++;
            }
        
        }
        
        ContenedorAMedida = new String[counter];
        
        for (int Q = 0; Q <= ContenedorAMedida.length - 1; Q++) {
        
            ContenedorAMedida[Q] = Mano[Q];
        
        }

        for(int i = 0; i < ContenedorAMedida.length; i++) {
            SToInt = ContenedorAMedida[i].substring(0,ContenedorAMedida[i].length()-2);
            if (SToInt.equals("J")||SToInt.equals("Q")||SToInt.equals("K")) {
                SToInt = 10+"";
            }
            if (SToInt.equals("A") && Converted+11 > 21) {
                SToInt = 1+"";
            }
            else{
                if (SToInt.equals("A") && Converted+11 <= 21) {
                    SToInt = 11+"";
                }
            }
            if(esNumero(SToInt))
            {
                Converted += Integer.parseInt(SToInt);
            }
            else{
                System.err.println("Algo salio mal: " + SToInt + " no es un numero");
            }
            
        }
        return Converted;
    }

    /* chequeo de que es numero */

    private static boolean esNumero(String NumOLet)
    {
        //Declara la posicion del Analicis de checkeo
        ParsePosition Lugar = new ParsePosition(0);
        //saca la instalcia del formato de numero y analiza el lugar
        NumberFormat.getInstance().parse(NumOLet,Lugar);
        //Positivo si el tamaño del lugar es igual a la posicion actual del analizador
        return NumOLet.length() == Lugar.getIndex();
    }


}
