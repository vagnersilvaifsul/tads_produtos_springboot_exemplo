public class Item {
    private Long id;
    private BigDecimal quantidade;
    private BigDecimal totalItem;
    private Byte situacao;

    //Associações
    private Pedido pedido;
    private Produto produto;
}
