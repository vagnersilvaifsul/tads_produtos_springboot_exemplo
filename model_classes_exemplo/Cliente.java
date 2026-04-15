public class Cliente {
    private Long id;
    private String nome;
    private String sobrenome;
    private Byte situacao;

    //Associações
    private Collection<Pedido> pedidos;
}
