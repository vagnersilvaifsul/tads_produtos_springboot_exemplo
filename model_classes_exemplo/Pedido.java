public class Pedido {
    private Long id;
    private String pagamento;
    private String estado;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private Byte situacao;

    //Associações
    private BigDecimal totalPedido;
    private Collection<Item> items;
    private Cliente cliente;
}
