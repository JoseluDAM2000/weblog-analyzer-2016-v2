import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AnalizadorAccesosAServidor
{
    private ArrayList<Acceso> accesos;
    private static final String HTTP_OK = "200";

    public AnalizadorAccesosAServidor() 
    {
        accesos = new ArrayList<>();
    }

    public void analizarArchivoDeLog(String archivo)
    {
        accesos.clear();
        File archivoALeer = new File(archivo);
        try {
            Scanner sc = new Scanner(archivoALeer);
            while (sc.hasNextLine()) {
                Acceso accesoActual = new Acceso(sc.nextLine());
                accesos.add(accesoActual);
            }
            sc.close();
        }
        catch (Exception e) {
            System.out.println("Ocurrio algun error al leer el archivo.");
        }
    }

    public int obtenerHoraMasAccesos() 
    {
        int valorADevolver = -1;

        if (!accesos.isEmpty()) {
            int[] accesosPorHora = new int[24];

            for (Acceso accesoActual : accesos) {
                int horaAccesoActual = accesoActual.getHora();
                accesosPorHora[horaAccesoActual] = accesosPorHora[horaAccesoActual] + 1;
            }

            int numeroDeAccesosMasAlto = accesosPorHora[0];
            int horaDeAccesosMasAlto = 0;
            for (int i = 0; i < accesosPorHora.length; i++) {
                if (accesosPorHora[i] >= numeroDeAccesosMasAlto) {
                    numeroDeAccesosMasAlto = accesosPorHora[i];
                    horaDeAccesosMasAlto = i;
                }
            }

            valorADevolver = horaDeAccesosMasAlto;                      
        }

        return valorADevolver;
    }

    public String paginaWebMasSolicitada() 
    {
        String valorADevolver = null;
        if (!accesos.isEmpty()) {
            HashMap<String, Integer> paginasSolicitadas = new HashMap<String, Integer>();
            for (Acceso accesoActual : accesos) {
                String paginaActual = accesoActual.getPagina();
                if(!paginasSolicitadas.containsKey(paginaActual)){
                    paginasSolicitadas.put(paginaActual, 1);
                }else{
                    paginasSolicitadas.put(paginaActual, paginasSolicitadas.get(paginaActual) + 1);
                }
            }
            String paginaMasVisitada = null;
            Iterator<String> it = paginasSolicitadas.keySet().iterator();
            while (it.hasNext()) {
                String paginaActual =it.next();
                if(paginaMasVisitada == null){
                    paginaMasVisitada = paginaActual;
                }else{
                    if(paginasSolicitadas.get(paginaActual) >= paginasSolicitadas.get(paginaMasVisitada)) {
                        paginaMasVisitada = paginaActual;
                    }
                }
            }
            valorADevolver = paginaMasVisitada;
        }else{
            System.out.println("Aun no se recibieron datos.");
        }
        return valorADevolver;
    }

    public String clienteConMasAccesosExitosos()
    {
        String valorADevolver = null;
        if (!accesos.isEmpty()) {
            HashMap<String, Integer> clientesConectados = new HashMap<String, Integer>();
            Iterator<Acceso> ite = accesos.iterator();
            while (ite.hasNext()) {
                Acceso accesoActual = ite.next();
                System.out.println(accesoActual.getCodigo());
                System.out.println(HTTP_OK);
                if(accesoActual.getCodigo().equals(HTTP_OK)){
                    String clienteActual = accesoActual.getIp();
                    if(!clientesConectados.containsKey(clienteActual)){
                        clientesConectados.put(clienteActual, 1);
                    }else{
                        clientesConectados.put(clienteActual, clientesConectados.get(clienteActual) + 1);
                    }
                }
            }
            String clienteConMasAccesos = null;
            Iterator<String> it = clientesConectados.keySet().iterator();
            while (it.hasNext()) {
                String ipActual = it.next();
                if(clienteConMasAccesos == null){
                    clienteConMasAccesos = ipActual;
                }else{
                    if(clientesConectados.get(ipActual) > clientesConectados.get(clienteConMasAccesos)) {
                        clienteConMasAccesos = ipActual;
                    } else if(clientesConectados.get(ipActual) == clientesConectados.get(clienteConMasAccesos)){
                        String[] octetosIpActual = ipActual.split("\\.");
                        String[] octetosIpMasAccesos = clienteConMasAccesos.split("\\.");
                        if(Integer.parseInt(octetosIpActual[3]) > Integer.parseInt(octetosIpMasAccesos[3])){
                            clienteConMasAccesos = ipActual;
                        }
                    }
                }
            }
            valorADevolver = clienteConMasAccesos;
        }else{
            System.out.println("Aun no se recibieron datos.");
        }
        return valorADevolver;
    }
}
