package mobiletcc.curso.br.mobilesecretarytcc.Modelo;

public class Consulta {
    private String idConsulta;
    private long timeStamp;
    private String Dia;
    private String Hora;
    private String idUser;
    private String emailUser;
    public Consulta() {
    }
    public String getIdConsulta() {
        return idConsulta;
    }
    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    @Override
    public String toString() {
        return "\n       Dia: " + Dia + "     |     Hora: " + Hora + "\n       Cliente: "+ emailUser +"\n";
    }
}
