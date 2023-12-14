import java.util.Random;
import java.util.Scanner;

public class Rescate {
    public static void main(String[] args) {
        // Creamos es scanner
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduce el numero de personas atrapadas: ");
        int numPersonasAtrapadas = sc.nextInt();
        sc.nextLine();
        Montania everest = new Montania(numPersonasAtrapadas);

        System.out.println("Introduce el numero de helicopteros: ");
        int numHelicopteros = sc.nextInt();
        Helicoptero[] helicopteros = new Helicoptero[numHelicopteros];

        System.out.println("Introduce la capacidad de cada Helicoptero: ");
        for (int i = 0; i < helicopteros.length; i++) {
            System.out.println("Capacidad del helicoptero numero " + (i + 1));
            helicopteros[i] = new Helicoptero(sc.nextInt(), everest);
        }

        for (int i = 0; i < helicopteros.length; i++) {
            helicopteros[i].start();
        }

        for (int i = 0; i < helicopteros.length; i++) {
            try {
                helicopteros[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sc.close();
    }
}

class Montania {
    private int personasAtrapadas;

    public Montania(int personasAtrapadas) {
        this.personasAtrapadas = personasAtrapadas;
    }

    public int getPersonasAtrapadas() {
        return personasAtrapadas;
    }

    public synchronized int rescatarPersonas(Helicoptero helicoptero) {
        System.out.println(helicoptero.getName() + " : estoy en la cima. Procedo al rescate");
        int rescatados = 0;
        try {
            Thread.sleep(new Random().nextInt(2000, 4000));
            if (helicoptero.getCapacidad() > personasAtrapadas) {
                rescatados = personasAtrapadas;
            } else {
                rescatados = helicoptero.getCapacidad();
            }

            personasAtrapadas -= rescatados;
            System.out.println(helicoptero.getName() + " : estoy en la cima. He rescatado a " + rescatados
                    + " atrapados. Quedan " + personasAtrapadas);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rescatados;
    }
}

class Helicoptero extends Thread {
    private int capacidad;
    private Montania montania;

    public Helicoptero(int capacidad, Montania montania) {
        this.capacidad = capacidad;
        this.montania = montania;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public void run() {
        Random secRandom = new Random();
        while (montania.getPersonasAtrapadas() > 0) {
            try {
                // Ida
                System.out.println(getName() + " : vamos a la cima");
                Thread.sleep(secRandom.nextInt(2000, 4000));
                // Carga
                int rescatados = montania.rescatarPersonas(this);
                // Vuelta
                System.out.println(getName() + " : vamos a la base");
                Thread.sleep(secRandom.nextInt(2000, 4000));
                // Descarga
                System.out.println(getName() + ": estoy en la base, he rescatado a " + rescatados);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
