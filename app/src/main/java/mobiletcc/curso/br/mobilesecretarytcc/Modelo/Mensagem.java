package mobiletcc.curso.br.mobilesecretarytcc.Modelo;

public class Mensagem {
    private String id_mensagem;
    private String idUser;
    private String emailUser;
    private String idAdm;
    private long timeStamp;
    private String texto_mensagem;

    public Mensagem() {
    }

    public String getId_mensagem() {
        return id_mensagem;
    }

    public void setId_mensagem(String id_mensagem) {
        this.id_mensagem = id_mensagem;
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

    public String getIdAdm() {
        return idAdm;
    }

    public void setIdAdm(String idAdm) {
        this.idAdm = idAdm;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTexto_mensagem() {
        return texto_mensagem;
    }

    public void setTexto_mensagem(String texto_mensagem) {
        this.texto_mensagem = texto_mensagem;
    }

    @Override
    public String toString() {
        return
                 emailUser + " diz: \n" + texto_mensagem;
    }
}
