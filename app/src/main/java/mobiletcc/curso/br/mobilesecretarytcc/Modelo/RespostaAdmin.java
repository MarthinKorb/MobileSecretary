package mobiletcc.curso.br.mobilesecretarytcc.Modelo;

public class RespostaAdmin {

    private String idMensagem;
    private String emitente;
    private String idDestinatario;
    private String destinatario;
    private String mensagemResposta;
    private long timeStamp;


    public RespostaAdmin() {
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getEmitente() {
        return emitente;
    }

    public void setEmitente(String emitente) {
        this.emitente = emitente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMensagemResposta() {
        return mensagemResposta;
    }

    public void setMensagemResposta(String mensagemResposta) {
        this.mensagemResposta = mensagemResposta;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return  "Admin" + " diz..\n"+
                "\n" + mensagemResposta + "\n";
    }
}
