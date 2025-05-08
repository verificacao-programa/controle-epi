import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;

/**
 * Sistema de Controle de EPIs (Equipamentos de Proteção Individual)
 *
 * Este sistema permite gerenciar:
 * - Cadastro de EPIs
 * - Cadastro de Funcionários
 * - Controle de empréstimos e devoluções
 * - Geração de relatórios
 *
 * Melhorias implementadas:
 * 1. Comentários detalhados em todas as funções
 * 2. Validação de entradas do usuário
 * 3. Tratamento de erros aprimorado
 * 4. Interface mais amigável
 * 5. Funcionalidades adicionais como verificação de validade de EPIs
 */
class ControleEPI {
    // Configurações de conexão com o banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/controle_epi";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // Altere para sua senha

    // Cores para melhorar a interface (ANSI escape codes)
    private static final String RESET = "\u001B[0m";
    private static final String VERDE = "\u001B[32m";
    private static final String VERMELHO = "\u001B[31m";
    private static final String AZUL = "\u001B[34m";
    private static final String AMARELO = "\u001B[33m";
    private static final String CIANO = "\u001B[36m";

    /**
     * Método principal que inicia o sistema
     */
    public static void main(String[] args) {
        exibirBannerInicial();
        testarConexao();
        menuPrincipal();
    }

    /**
     * Exibe um banner inicial com informações do sistema
     */
    private static void exibirBannerInicial() {
        System.out.println(CIANO + "==============================================");
        System.out.println("       SISTEMA DE CONTROLE DE EPIs v2.0");
        System.out.println("==============================================" + RESET);
        System.out.println("Desenvolvido para gestão de Equipamentos de");
        System.out.println("Proteção Individual em conformidade com normas");
        System.out.println("de segurança do trabalho");
        System.out.println(CIANO + "==============================================" + RESET);
    }

    /**
     * Testa a conexão com o banco de dados
     * Exibe mensagem de sucesso ou erro e encerra o sistema em caso de falha
     */
    public static void testarConexao() {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            System.out.println(VERDE + "\n✅ Conexão com o banco de dados estabelecida com sucesso!" + RESET);
        } catch (SQLException e) {
            System.err.println(VERMELHO + "\n❌ Falha na conexão com o banco de dados: " + e.getMessage() + RESET);
            System.err.println("Verifique se:");
            System.err.println("1. O MySQL está rodando");
            System.err.println("2. O banco 'controle_epi' existe");
            System.err.println("3. As credenciais estão corretas no código");
            System.exit(1); // Encerra o programa com código de erro
        }
    }

    /**
     * Menu principal do sistema com opções numeradas
     */
    public static void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n" + AZUL + "===== MENU PRINCIPAL =====" + RESET);
            System.out.println("1. 📦 Gerenciar EPIs");
            System.out.println("2. 👥 Gerenciar Funcionários");
            System.out.println("3. 🔄 Gerenciar Empréstimos");
            System.out.println("4. ↩️ Registrar Devolução");
            System.out.println("5. 📊 Relatórios");
            System.out.println("6. ⏳ Verificar EPIs próximos da validade");
            System.out.println("0. 🚪 Sair");
            System.out.print(AMARELO + "Escolha uma opção: " + RESET);

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        menuEPI(scanner);
                        break;
                    case 2:
                        menuFuncionario(scanner);
                        break;
                    case 3:
                        menuEmprestimo(scanner);
                        break;
                    case 4:
                        registrarDevolucao(scanner);
                        break;
                    case 5:
                        menuRelatorios(scanner);
                        break;
                    case 6:
                        verificarEPIsProximosValidade();
                        break;
                    case 0:
                        System.out.println(VERDE + "\nSaindo do sistema... Obrigado por utilizar!" + RESET);
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println(VERMELHO + "Opção inválida! Por favor, tente novamente." + RESET);
                }
            } catch (Exception e) {
                System.out.println(VERMELHO + "Entrada inválida! Digite um número correspondente às opções." + RESET);
                scanner.nextLine(); // Limpa o buffer em caso de erro
            }
        }
    }

    /**
     * Menu de gerenciamento de EPIs
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void menuEPI(Scanner scanner) {
        while (true) {
            System.out.println("\n" + AZUL + "===== GERENCIAMENTO DE EPIs =====" + RESET);
            System.out.println("1. ➕ Cadastrar novo EPI");
            System.out.println("2. 📋 Listar todos os EPIs");
            System.out.println("3. 🔍 Buscar EPI por ID");
            System.out.println("4. ✏️ Atualizar EPI");
            System.out.println("5. ❌ Remover EPI");
            System.out.println("6. ⚠️ Ver EPIs vencidos");
            System.out.println("0. ↩️ Voltar ao menu principal");
            System.out.print(AMARELO + "Escolha uma opção: " + RESET);

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        cadastrarEPI(scanner);
                        break;
                    case 2:
                        listarEPIs();
                        break;
                    case 3:
                        buscarEPIPorId(scanner);
                        break;
                    case 4:
                        atualizarEPI(scanner);
                        break;
                    case 5:
                        removerEPI(scanner);
                        break;
                    case 6:
                        listarEPIsVencidos();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                }
            } catch (Exception e) {
                System.out.println(VERMELHO + "Entrada inválida! Digite um número." + RESET);
                scanner.nextLine();
            }
        }
    }

    /**
     * Menu de gerenciamento de funcionários
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void menuFuncionario(Scanner scanner) {
        while (true) {
            System.out.println("\n" + AZUL + "===== GERENCIAMENTO DE FUNCIONÁRIOS =====" + RESET);
            System.out.println("1. ➕ Cadastrar novo funcionário");
            System.out.println("2. 📋 Listar todos os funcionários");
            System.out.println("3. 🔍 Buscar funcionário por ID");
            System.out.println("4. ✏️ Atualizar funcionário");
            System.out.println("5. ❌ Remover funcionário");
            System.out.println("0. ↩️ Voltar ao menu principal");
            System.out.print(AMARELO + "Escolha uma opção: " + RESET);

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        cadastrarFuncionario(scanner);
                        break;
                    case 2:
                        listarFuncionarios();
                        break;
                    case 3:
                        buscarFuncionarioPorId(scanner);
                        break;
                    case 4:
                        atualizarFuncionario(scanner);
                        break;
                    case 5:
                        removerFuncionario(scanner);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                }
            } catch (Exception e) {
                System.out.println(VERMELHO + "Entrada inválida! Digite um número." + RESET);
                scanner.nextLine();
            }
        }
    }

    /**
     * Menu de gerenciamento de empréstimos
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void menuEmprestimo(Scanner scanner) {
        while (true) {
            System.out.println("\n" + AZUL + "===== GERENCIAMENTO DE EMPRÉSTIMOS =====" + RESET);
            System.out.println("1. ➕ Registrar novo empréstimo");
            System.out.println("2. 📋 Listar todos os empréstimos");
            System.out.println("3. 🔍 Buscar empréstimo por ID");
            System.out.println("4. ✅ Listar empréstimos ativos");
            System.out.println("5. ⏳ Listar empréstimos próximos do vencimento");
            System.out.println("0. ↩️ Voltar ao menu principal");
            System.out.print(AMARELO + "Escolha uma opção: " + RESET);

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        registrarEmprestimo(scanner);
                        break;
                    case 2:
                        listarEmprestimos();
                        break;
                    case 3:
                        buscarEmprestimoPorId(scanner);
                        break;
                    case 4:
                        listarEmprestimosAtivos();
                        break;
                    case 5:
                        listarEmprestimosProximosVencimento();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                }
            } catch (Exception e) {
                System.out.println(VERMELHO + "Entrada inválida! Digite um número." + RESET);
                scanner.nextLine();
            }
        }
    }

    /**
     * Menu de relatórios
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void menuRelatorios(Scanner scanner) {
        while (true) {
            System.out.println("\n" + AZUL + "===== RELATÓRIOS =====" + RESET);
            System.out.println("1. 📦 EPIs disponíveis");
            System.out.println("2. 🚀 EPIs emprestados");
            System.out.println("3. 👤 Histórico de empréstimos por funcionário");
            System.out.println("4. 📦 Histórico de empréstimos por EPI");
            System.out.println("5. ⏳ EPIs com validade expirando em 30 dias");
            System.out.println("0. ↩️ Voltar ao menu principal");
            System.out.print(AMARELO + "Escolha uma opção: " + RESET);

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        listarEPIsDisponiveis();
                        break;
                    case 2:
                        listarEPIsEmprestados();
                        break;
                    case 3:
                        historicoEmprestimosPorFuncionario(scanner);
                        break;
                    case 4:
                        historicoEmprestimosPorEPI(scanner);
                        break;
                    case 5:
                        verificarEPIsProximosValidade();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                }
            } catch (Exception e) {
                System.out.println(VERMELHO + "Entrada inválida! Digite um número." + RESET);
                scanner.nextLine();
            }
        }
    }

    // ========== MÉTODOS PARA EPIs ==========

    /**
     * Cadastra um novo EPI no sistema
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void cadastrarEPI(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- CADASTRAR NOVO EPI ---" + RESET);

        try {
            System.out.print("Nome do EPI: ");
            String nome = scanner.nextLine();

            if (nome.isEmpty()) {
                System.out.println(VERMELHO + "O nome do EPI não pode estar vazio!" + RESET);
                return;
            }

            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            System.out.print("Data de validade (AAAA-MM-DD): ");
            String validade = scanner.nextLine();

            // Validação simples da data
            if (!validade.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println(VERMELHO + "Formato de data inválido! Use AAAA-MM-DD." + RESET);
                return;
            }

            System.out.print("Quantidade em estoque: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            if (quantidade < 0) {
                System.out.println(VERMELHO + "A quantidade não pode ser negativa!" + RESET);
                return;
            }

            // Verifica se o EPI já existe
            if (epiExiste(nome)) {
                System.out.println(AMARELO + "Este EPI já está cadastrado. Deseja adicionar mais unidades? (S/N)" + RESET);
                String resposta = scanner.nextLine();

                if (resposta.equalsIgnoreCase("S")) {
                    adicionarEstoqueEPI(nome, quantidade);
                    return;
                } else {
                    System.out.println("Operação cancelada.");
                    return;
                }
            }

            String sql = "INSERT INTO epi (nome, descricao, validade, quantidade) VALUES (?, ?, ?, ?)";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, nome);
                stmt.setString(2, descricao);
                stmt.setString(3, validade);
                stmt.setInt(4, quantidade);
                stmt.executeUpdate();

                // Recupera o ID gerado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println(VERDE + "EPI cadastrado com sucesso! ID: " + generatedKeys.getInt(1) + RESET);
                    } else {
                        System.out.println(VERDE + "EPI cadastrado com sucesso!" + RESET);
                    }
                }
            } catch (SQLException e) {
                System.err.println(VERMELHO + "Erro ao cadastrar EPI: " + e.getMessage() + RESET);
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Entrada inválida! Por favor, tente novamente." + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Lista todos os EPIs cadastrados no sistema
     */
    public static void listarEPIs() {
        System.out.println("\n" + AZUL + "--- LISTA DE EPIs ---" + RESET);
        String sql = "SELECT * FROM epi ORDER BY nome";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-30s %-20s %-15s %-10s%n", "ID", "Nome", "Descrição", "Validade", "Quantidade");
            System.out.println("--------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                Date validade = rs.getDate("validade");
                int quantidade = rs.getInt("quantidade");

                // Verifica se o EPI está vencido
                LocalDate hoje = LocalDate.now();
                LocalDate dataValidade = validade.toLocalDate();
                String statusValidade = dataValidade.isBefore(hoje) ? VERMELHO + "VENCIDO" + RESET : VERDE + "OK" + RESET;

                System.out.printf("%-5d %-30s %-20s %-15s %-10d %s%n",
                        id, nome, descricao, validade, quantidade, statusValidade);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar EPIs: " + e.getMessage() + RESET);
        }
    }

    /**
     * Busca um EPI específico pelo seu ID
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void buscarEPIPorId(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- BUSCAR EPI POR ID ---" + RESET);
        System.out.print("Digite o ID do EPI: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            String sql = "SELECT * FROM epi WHERE id = ?";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("\n" + AZUL + "DETALHES DO EPI:" + RESET);
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nome: " + rs.getString("nome"));
                    System.out.println("Descrição: " + rs.getString("descricao"));

                    Date validade = rs.getDate("validade");
                    LocalDate hoje = LocalDate.now();
                    LocalDate dataValidade = validade.toLocalDate();

                    if (dataValidade.isBefore(hoje)) {
                        System.out.println("Validade: " + validade + " " + VERMELHO + "(VENCIDO)" + RESET);
                    } else if (dataValidade.isBefore(hoje.plusDays(30))) {
                        System.out.println("Validade: " + validade + " " + AMARELO + "(PRÓXIMO DO VENCIMENTO)" + RESET);
                    } else {
                        System.out.println("Validade: " + validade + " " + VERDE + "(DENTRO DO PRAZO)" + RESET);
                    }

                    System.out.println("Quantidade: " + rs.getInt("quantidade"));

                    // Mostra histórico de empréstimos para este EPI
                    System.out.println("\n" + AZUL + "ÚLTIMOS EMPRÉSTIMOS:" + RESET);
                    historicoEmprestimosPorEPI(id);
                } else {
                    System.out.println(VERMELHO + "EPI não encontrado com o ID: " + id + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "ID inválido! Digite um número." + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Atualiza os dados de um EPI existente
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void atualizarEPI(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- ATUALIZAR EPI ---" + RESET);
        System.out.print("Digite o ID do EPI que deseja atualizar: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            // Primeiro verifica se o EPI existe
            if (!epiExiste(id)) {
                System.out.println(VERMELHO + "EPI não encontrado com o ID: " + id + RESET);
                return;
            }

            System.out.print("Novo nome do EPI (deixe em branco para não alterar): ");
            String nome = scanner.nextLine();

            System.out.print("Nova descrição (deixe em branco para não alterar): ");
            String descricao = scanner.nextLine();

            System.out.print("Nova data de validade (AAAA-MM-DD) (deixe em branco para não alterar): ");
            String validadeStr = scanner.nextLine();
            Date validade = null;

            if (!validadeStr.isEmpty()) {
                if (!validadeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println(VERMELHO + "Formato de data inválido! Use AAAA-MM-DD." + RESET);
                    return;
                }
                validade = Date.valueOf(validadeStr);
            }

            System.out.print("Nova quantidade em estoque (digite -1 para não alterar): ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            // Construir a query dinamicamente com base nos campos que serão atualizados
            StringBuilder sql = new StringBuilder("UPDATE epi SET ");
            boolean temCampoParaAtualizar = false;

            if (!nome.isEmpty()) {
                sql.append("nome = ?, ");
                temCampoParaAtualizar = true;
            }
            if (!descricao.isEmpty()) {
                sql.append("descricao = ?, ");
                temCampoParaAtualizar = true;
            }
            if (validade != null) {
                sql.append("validade = ?, ");
                temCampoParaAtualizar = true;
            }
            if (quantidade != -1) {
                sql.append("quantidade = ?, ");
                temCampoParaAtualizar = true;
            }

            if (!temCampoParaAtualizar) {
                System.out.println(AMARELO + "Nenhum campo foi alterado." + RESET);
                return;
            }

            // Remove a última vírgula e espaço
            sql.delete(sql.length() - 2, sql.length());
            sql.append(" WHERE id = ?");

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql.toString())) {

                int paramIndex = 1;

                if (!nome.isEmpty()) {
                    stmt.setString(paramIndex++, nome);
                }
                if (!descricao.isEmpty()) {
                    stmt.setString(paramIndex++, descricao);
                }
                if (validade != null) {
                    stmt.setDate(paramIndex++, validade);
                }
                if (quantidade != -1) {
                    stmt.setInt(paramIndex++, quantidade);
                }

                stmt.setInt(paramIndex, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println(VERDE + "EPI atualizado com sucesso!" + RESET);
                } else {
                    System.out.println(VERMELHO + "Nenhum EPI foi atualizado." + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao atualizar EPI: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Remove um EPI do sistema
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void removerEPI(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- REMOVER EPI ---" + RESET);
        System.out.print("Digite o ID do EPI que deseja remover: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            // Verifica se o EPI existe
            if (!epiExiste(id)) {
                System.out.println(VERMELHO + "EPI não encontrado com o ID: " + id + RESET);
                return;
            }

            // Verifica se há empréstimos ativos para este EPI
            if (temEmprestimosAtivos(id)) {
                System.out.println(VERMELHO + "Não é possível remover este EPI pois existem empréstimos ativos relacionados a ele." + RESET);
                return;
            }

            System.out.print(VERMELHO + "Tem certeza que deseja remover este EPI? (S/N): " + RESET);
            String confirmacao = scanner.nextLine();

            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("Operação cancelada.");
                return;
            }

            String sql = "DELETE FROM epi WHERE id = ?";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println(VERDE + "EPI removido com sucesso!" + RESET);
                } else {
                    System.out.println(VERMELHO + "Nenhum EPI foi removido." + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao remover EPI: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Lista EPIs que estão vencidos
     */
    public static void listarEPIsVencidos() {
        System.out.println("\n" + AZUL + "--- EPIs VENCIDOS ---" + RESET);
        String sql = "SELECT * FROM epi WHERE validade < CURDATE() ORDER BY validade";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-30s %-15s %-10s%n", "ID", "Nome", "Validade", "Quantidade");
            System.out.println("------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                Date validade = rs.getDate("validade");
                int quantidade = rs.getInt("quantidade");

                System.out.printf("%-5d %-30s %-15s %-10d %s%n",
                        id, nome, validade, quantidade, VERMELHO + "VENCIDO" + RESET);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar EPIs vencidos: " + e.getMessage() + RESET);
        }
    }

    /**
     * Verifica se um EPI existe pelo ID
     * @param id ID do EPI
     * @return true se existe, false caso contrário
     */
    private static boolean epiExiste(int id) {
        String sql = "SELECT 1 FROM epi WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar EPI: " + e.getMessage() + RESET);
            return false;
        }
    }

    /**
     * Verifica se um EPI existe pelo nome
     * @param nome Nome do EPI
     * @return true se existe, false caso contrário
     */
    private static boolean epiExiste(String nome) {
        String sql = "SELECT 1 FROM epi WHERE nome = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar EPI: " + e.getMessage() + RESET);
            return false;
        }
    }

    /**
     * Verifica se há empréstimos ativos para um EPI
     * @param idEpi ID do EPI
     * @return true se há empréstimos ativos, false caso contrário
     */
    private static boolean temEmprestimosAtivos(int idEpi) {
        String sql = "SELECT 1 FROM emprestimo WHERE id_epi = ? AND status = 'Ativo'";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idEpi);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar empréstimos: " + e.getMessage() + RESET);
            return true; // Assume que há empréstimos para evitar remoção acidental
        }
    }

    /**
     * Adiciona estoque a um EPI existente
     * @param nome Nome do EPI
     * @param quantidade Quantidade a ser adicionada
     */
    private static void adicionarEstoqueEPI(String nome, int quantidade) {
        String sql = "UPDATE epi SET quantidade = quantidade + ? WHERE nome = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, quantidade);
            stmt.setString(2, nome);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println(VERDE + "Estoque atualizado com sucesso!" + RESET);
            } else {
                System.out.println(VERMELHO + "Falha ao atualizar estoque." + RESET);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao atualizar estoque: " + e.getMessage() + RESET);
        }
    }

    // ========== MÉTODOS PARA FUNCIONÁRIOS ==========

    /**
     * Cadastra um novo funcionário no sistema
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void cadastrarFuncionario(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- CADASTRAR NOVO FUNCIONÁRIO ---" + RESET);

        try {
            System.out.print("Nome do funcionário: ");
            String nome = scanner.nextLine();

            if (nome.isEmpty()) {
                System.out.println(VERMELHO + "O nome não pode estar vazio!" + RESET);
                return;
            }

            System.out.print("CPF (somente números): ");
            String cpf = scanner.nextLine();

            // Validação simples de CPF
            if (!cpf.matches("\\d{11}")) {
                System.out.println(VERMELHO + "CPF inválido! Deve conter 11 dígitos." + RESET);
                return;
            }

            // Verifica se CPF já está cadastrado
            if (cpfExiste(cpf)) {
                System.out.println(VERMELHO + "Este CPF já está cadastrado no sistema!" + RESET);
                return;
            }

            System.out.print("Cargo: ");
            String cargo = scanner.nextLine();

            System.out.print("Departamento: ");
            String departamento = scanner.nextLine();

            String sql = "INSERT INTO funcionario (nome, cpf, cargo, departamento) VALUES (?, ?, ?, ?)";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, nome);
                stmt.setString(2, cpf);
                stmt.setString(3, cargo);
                stmt.setString(4, departamento);
                stmt.executeUpdate();

                // Recupera o ID gerado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println(VERDE + "Funcionário cadastrado com sucesso! ID: " + generatedKeys.getInt(1) + RESET);
                    } else {
                        System.out.println(VERDE + "Funcionário cadastrado com sucesso!" + RESET);
                    }
                }
            } catch (SQLException e) {
                System.err.println(VERMELHO + "Erro ao cadastrar funcionário: " + e.getMessage() + RESET);
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao cadastrar funcionário: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Lista todos os funcionários cadastrados no sistema
     */
    public static void listarFuncionarios() {
        System.out.println("\n" + AZUL + "--- LISTA DE FUNCIONÁRIOS ---" + RESET);
        String sql = "SELECT * FROM funcionario ORDER BY nome";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-30s %-15s %-20s %-15s%n", "ID", "Nome", "CPF", "Cargo", "Departamento");
            System.out.println("--------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String cargo = rs.getString("cargo");
                String departamento = rs.getString("departamento");

                // Formata o CPF para exibição
                String cpfFormatado = cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

                System.out.printf("%-5d %-30s %-15s %-20s %-15s%n", id, nome, cpfFormatado, cargo, departamento);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar funcionários: " + e.getMessage() + RESET);
        }
    }

    /**
     * Busca um funcionário específico pelo seu ID
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void buscarFuncionarioPorId(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- BUSCAR FUNCIONÁRIO POR ID ---" + RESET);
        System.out.print("Digite o ID do funcionário: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            String sql = "SELECT * FROM funcionario WHERE id = ?";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("\n" + AZUL + "DETALHES DO FUNCIONÁRIO:" + RESET);
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nome: " + rs.getString("nome"));

                    // Formata o CPF para exibição
                    String cpf = rs.getString("cpf");
                    String cpfFormatado = cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                    System.out.println("CPF: " + cpfFormatado);

                    System.out.println("Cargo: " + rs.getString("cargo"));
                    System.out.println("Departamento: " + rs.getString("departamento"));

                    // Mostra histórico de empréstimos para este funcionário
                    System.out.println("\n" + AZUL + "ÚLTIMOS EMPRÉSTIMOS:" + RESET);
                    historicoEmprestimosPorFuncionario(id);
                } else {
                    System.out.println(VERMELHO + "Funcionário não encontrado com o ID: " + id + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "ID inválido! Digite um número." + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Atualiza os dados de um funcionário existente
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void atualizarFuncionario(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- ATUALIZAR FUNCIONÁRIO ---" + RESET);
        System.out.print("Digite o ID do funcionário que deseja atualizar: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            // Primeiro verifica se o funcionário existe
            if (!funcionarioExiste(id)) {
                System.out.println(VERMELHO + "Funcionário não encontrado com o ID: " + id + RESET);
                return;
            }

            System.out.print("Novo nome (deixe em branco para não alterar): ");
            String nome = scanner.nextLine();

            System.out.print("Novo CPF (deixe em branco para não alterar): ");
            String cpf = scanner.nextLine();

            if (!cpf.isEmpty() && !cpf.matches("\\d{11}")) {
                System.out.println(VERMELHO + "CPF inválido! Deve conter 11 dígitos." + RESET);
                return;
            }

            System.out.print("Novo cargo (deixe em branco para não alterar): ");
            String cargo = scanner.nextLine();

            System.out.print("Novo departamento (deixe em branco para não alterar): ");
            String departamento = scanner.nextLine();

            // Construir a query dinamicamente com base nos campos que serão atualizados
            StringBuilder sql = new StringBuilder("UPDATE funcionario SET ");
            boolean temCampoParaAtualizar = false;

            if (!nome.isEmpty()) {
                sql.append("nome = ?, ");
                temCampoParaAtualizar = true;
            }
            if (!cpf.isEmpty()) {
                sql.append("cpf = ?, ");
                temCampoParaAtualizar = true;
            }
            if (!cargo.isEmpty()) {
                sql.append("cargo = ?, ");
                temCampoParaAtualizar = true;
            }
            if (!departamento.isEmpty()) {
                sql.append("departamento = ?, ");
                temCampoParaAtualizar = true;
            }

            if (!temCampoParaAtualizar) {
                System.out.println(AMARELO + "Nenhum campo foi alterado." + RESET);
                return;
            }

            // Remove a última vírgula e espaço
            sql.delete(sql.length() - 2, sql.length());
            sql.append(" WHERE id = ?");

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql.toString())) {

                int paramIndex = 1;

                if (!nome.isEmpty()) {
                    stmt.setString(paramIndex++, nome);
                }
                if (!cpf.isEmpty()) {
                    stmt.setString(paramIndex++, cpf);
                }
                if (!cargo.isEmpty()) {
                    stmt.setString(paramIndex++, cargo);
                }
                if (!departamento.isEmpty()) {
                    stmt.setString(paramIndex++, departamento);
                }

                stmt.setInt(paramIndex, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println(VERDE + "Funcionário atualizado com sucesso!" + RESET);
                } else {
                    System.out.println(VERMELHO + "Nenhum funcionário foi atualizado." + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao atualizar funcionário: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Remove um funcionário do sistema
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void removerFuncionario(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- REMOVER FUNCIONÁRIO ---" + RESET);
        System.out.print("Digite o ID do funcionário que deseja remover: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            // Verifica se o funcionário existe
            if (!funcionarioExiste(id)) {
                System.out.println(VERMELHO + "Funcionário não encontrado com o ID: " + id + RESET);
                return;
            }

            // Verifica se há empréstimos ativos para este funcionário
            if (temEmprestimosAtivosFuncionario(id)) {
                System.out.println(VERMELHO + "Não é possível remover este funcionário pois existem empréstimos ativos relacionados a ele." + RESET);
                return;
            }

            System.out.print(VERMELHO + "Tem certeza que deseja remover este funcionário? (S/N): " + RESET);
            String confirmacao = scanner.nextLine();

            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("Operação cancelada.");
                return;
            }

            String sql = "DELETE FROM funcionario WHERE id = ?";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println(VERDE + "Funcionário removido com sucesso!" + RESET);
                } else {
                    System.out.println(VERMELHO + "Nenhum funcionário foi removido." + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao remover funcionário: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Verifica se um funcionário existe pelo ID
     * @param id ID do funcionário
     * @return true se existe, false caso contrário
     */
    private static boolean funcionarioExiste(int id) {
        String sql = "SELECT 1 FROM funcionario WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar funcionário: " + e.getMessage() + RESET);
            return false;
        }
    }

    /**
     * Verifica se um CPF já está cadastrado
     * @param cpf CPF a ser verificado
     * @return true se existe, false caso contrário
     */
    private static boolean cpfExiste(String cpf) {
        String sql = "SELECT 1 FROM funcionario WHERE cpf = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar CPF: " + e.getMessage() + RESET);
            return true; // Assume que existe para evitar duplicação
        }
    }

    /**
     * Verifica se há empréstimos ativos para um funcionário
     * @param idFuncionario ID do funcionário
     * @return true se há empréstimos ativos, false caso contrário
     */
    private static boolean temEmprestimosAtivosFuncionario(int idFuncionario) {
        String sql = "SELECT 1 FROM emprestimo WHERE id_funcionario = ? AND status = 'Ativo'";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar empréstimos: " + e.getMessage() + RESET);
            return true; // Assume que há empréstimos para evitar remoção acidental
        }
    }

    // ========== MÉTODOS PARA EMPRÉSTIMOS ==========

    /**
     * Registra um novo empréstimo de EPI para um funcionário
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void registrarEmprestimo(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- REGISTRAR NOVO EMPRÉSTIMO ---" + RESET);

        try {
            // Listar funcionários disponíveis
            System.out.println("\n" + AZUL + "FUNCIONÁRIOS DISPONÍVEIS:" + RESET);
            listarFuncionarios();

            System.out.print("\nDigite o ID do funcionário: ");
            int idFuncionario = scanner.nextInt();
            scanner.nextLine();

            // Verifica se o funcionário existe
            if (!funcionarioExiste(idFuncionario)) {
                System.out.println(VERMELHO + "Funcionário não encontrado!" + RESET);
                return;
            }

            // Listar EPIs disponíveis
            System.out.println("\n" + AZUL + "EPIs DISPONÍVEIS:" + RESET);
            listarEPIsDisponiveis();

            System.out.print("\nDigite o ID do EPI: ");
            int idEPI = scanner.nextInt();
            scanner.nextLine();

            // Verifica se o EPI existe
            if (!epiExiste(idEPI)) {
                System.out.println(VERMELHO + "EPI não encontrado!" + RESET);
                return;
            }

            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            if (quantidade <= 0) {
                System.out.println(VERMELHO + "A quantidade deve ser maior que zero!" + RESET);
                return;
            }

            // Verificar se há quantidade suficiente e se o EPI está dentro da validade
            if (!verificarQuantidadeDisponivel(idEPI, quantidade)) {
                System.out.println(VERMELHO + "Quantidade indisponível para empréstimo!" + RESET);
                return;
            }

            if (epiVencido(idEPI)) {
                System.out.println(VERMELHO + "Este EPI está vencido e não pode ser emprestado!" + RESET);
                return;
            }

            LocalDate dataAtual = LocalDate.now();
            LocalDate dataDevolucaoPrevista = dataAtual.plusDays(30); // 30 dias para devolução

            String sql = "INSERT INTO emprestimo (id_funcionario, id_epi, data_emprestimo, " +
                    "data_devolucao_prevista, quantidade, status) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, idFuncionario);
                stmt.setInt(2, idEPI);
                stmt.setString(3, dataAtual.toString());
                stmt.setString(4, dataDevolucaoPrevista.toString());
                stmt.setInt(5, quantidade);
                stmt.setString(6, "Ativo");
                stmt.executeUpdate();

                // Atualizar estoque do EPI
                atualizarEstoqueEPI(idEPI, -quantidade);

                // Recupera o ID gerado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println(VERDE + "Empréstimo registrado com sucesso! Número: " + generatedKeys.getInt(1) + RESET);
                    } else {
                        System.out.println(VERDE + "Empréstimo registrado com sucesso!" + RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao registrar empréstimo: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Verifica se há quantidade suficiente de um EPI para empréstimo
     * @param idEPI ID do EPI
     * @param quantidade Quantidade desejada
     * @return true se há quantidade suficiente, false caso contrário
     */
    private static boolean verificarQuantidadeDisponivel(int idEPI, int quantidade) {
        String sql = "SELECT quantidade, validade FROM epi WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idEPI);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int quantidadeDisponivel = rs.getInt("quantidade");
                Date validade = rs.getDate("validade");

                // Verifica se o EPI está vencido
                LocalDate hoje = LocalDate.now();
                if (validade.toLocalDate().isBefore(hoje)) {
                    return false;
                }

                return quantidadeDisponivel >= quantidade;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar quantidade disponível: " + e.getMessage() + RESET);
            return false;
        }
    }

    /**
     * Verifica se um EPI está vencido
     * @param idEPI ID do EPI
     * @return true se está vencido, false caso contrário
     */
    private static boolean epiVencido(int idEPI) {
        String sql = "SELECT validade FROM epi WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idEPI);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date validade = rs.getDate("validade");
                LocalDate hoje = LocalDate.now();
                return validade.toLocalDate().isBefore(hoje);
            }
            return false;
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar validade do EPI: " + e.getMessage() + RESET);
            return true; // Assume que está vencido para evitar empréstimo
        }
    }

    /**
     * Atualiza o estoque de um EPI
     * @param idEPI ID do EPI
     * @param quantidade Quantidade a ser adicionada (pode ser negativa)
     */
    private static void atualizarEstoqueEPI(int idEPI, int quantidade) {
        String sql = "UPDATE epi SET quantidade = quantidade + ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, quantidade);
            stmt.setInt(2, idEPI);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao atualizar estoque do EPI: " + e.getMessage() + RESET);
        }
    }

    /**
     * Lista todos os empréstimos registrados no sistema
     */
    public static void listarEmprestimos() {
        System.out.println("\n" + AZUL + "--- LISTA DE EMPRÉSTIMOS ---" + RESET);
        String sql = "SELECT e.id, f.nome AS funcionario, ep.nome AS epi, e.data_emprestimo, " +
                "e.data_devolucao_prevista, e.data_devolucao_real, e.quantidade, e.status " +
                "FROM emprestimo e " +
                "JOIN funcionario f ON e.id_funcionario = f.id " +
                "JOIN epi ep ON e.id_epi = ep.id " +
                "ORDER BY e.data_emprestimo DESC";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-20s %-15s %-20s %-20s %-10s %-10s%n",
                    "ID", "Funcionário", "EPI", "Data Empréstimo", "Devolução Prevista",
                    "Devolução Real", "Quantidade", "Status");
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String funcionario = rs.getString("funcionario");
                String epi = rs.getString("epi");
                Date dataEmprestimo = rs.getDate("data_emprestimo");
                Date dataDevolucaoPrevista = rs.getDate("data_devolucao_prevista");
                Date dataDevolucaoReal = rs.getDate("data_devolucao_real");
                int quantidade = rs.getInt("quantidade");
                String status = rs.getString("status");

                // Formata o status com cores
                String statusFormatado = status.equals("Ativo") ? VERDE + status + RESET : CIANO + status + RESET;

                System.out.printf("%-5d %-20s %-20s %-15s %-20s %-20s %-10d %-10s%n",
                        id, funcionario, epi, dataEmprestimo, dataDevolucaoPrevista,
                        dataDevolucaoReal != null ? dataDevolucaoReal : "N/A", quantidade, statusFormatado);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar empréstimos: " + e.getMessage() + RESET);
        }
    }

    /**
     * Busca um empréstimo específico pelo seu ID
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void buscarEmprestimoPorId(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- BUSCAR EMPRÉSTIMO POR ID ---" + RESET);
        System.out.print("Digite o ID do empréstimo: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "SELECT e.id, f.nome AS funcionario, ep.nome AS epi, e.data_emprestimo, " +
                    "e.data_devolucao_prevista, e.data_devolucao_real, e.quantidade, e.status " +
                    "FROM emprestimo e " +
                    "JOIN funcionario f ON e.id_funcionario = f.id " +
                    "JOIN epi ep ON e.id_epi = ep.id " +
                    "WHERE e.id = ?";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("\n" + AZUL + "DETALHES DO EMPRÉSTIMO:" + RESET);
                    System.out.println("Número: " + rs.getInt("id"));
                    System.out.println("Funcionário: " + rs.getString("funcionario"));
                    System.out.println("EPI: " + rs.getString("epi"));
                    System.out.println("Data do Empréstimo: " + rs.getDate("data_emprestimo"));
                    System.out.println("Data de Devolução Prevista: " + rs.getDate("data_devolucao_prevista"));

                    Date dataDevolucaoReal = rs.getDate("data_devolucao_real");
                    String devolucaoRealStr = dataDevolucaoReal != null ? dataDevolucaoReal.toString() : "N/A";
                    System.out.println("Data de Devolução Real: " + devolucaoRealStr);

                    System.out.println("Quantidade: " + rs.getInt("quantidade"));

                    String status = rs.getString("status");
                    String statusFormatado = status.equals("Ativo") ? VERDE + status + RESET : CIANO + status + RESET;
                    System.out.println("Status: " + statusFormatado);

                    // Verifica se está atrasado
                    if (status.equals("Ativo")) {
                        LocalDate hoje = LocalDate.now();
                        LocalDate dataPrevista = rs.getDate("data_devolucao_prevista").toLocalDate();

                        if (hoje.isAfter(dataPrevista)) {
                            long diasAtraso = hoje.toEpochDay() - dataPrevista.toEpochDay();
                            System.out.println(VERMELHO + "EMPRÉSTIMO ATRASADO! (" + diasAtraso + " dias)" + RESET);
                        }
                    }
                } else {
                    System.out.println(VERMELHO + "Empréstimo não encontrado com o ID: " + id + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "ID inválido! Digite um número." + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Lista todos os empréstimos ativos no sistema
     */
    public static void listarEmprestimosAtivos() {
        System.out.println("\n" + AZUL + "--- EMPRÉSTIMOS ATIVOS ---" + RESET);
        String sql = "SELECT e.id, f.nome AS funcionario, ep.nome AS epi, e.data_emprestimo, " +
                "e.data_devolucao_prevista, e.quantidade " +
                "FROM emprestimo e " +
                "JOIN funcionario f ON e.id_funcionario = f.id " +
                "JOIN epi ep ON e.id_epi = ep.id " +
                "WHERE e.status = 'Ativo' " +
                "ORDER BY e.data_devolucao_prevista";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-20s %-15s %-20s %-10s%n",
                    "ID", "Funcionário", "EPI", "Data Empréstimo", "Devolução Prevista", "Quantidade");
            System.out.println("--------------------------------------------------------------------------------");

            LocalDate hoje = LocalDate.now();

            while (rs.next()) {
                int id = rs.getInt("id");
                String funcionario = rs.getString("funcionario");
                String epi = rs.getString("epi");
                Date dataEmprestimo = rs.getDate("data_emprestimo");
                Date dataDevolucaoPrevista = rs.getDate("data_devolucao_prevista");
                int quantidade = rs.getInt("quantidade");

                // Verifica se está atrasado
                String statusData = "";
                if (hoje.isAfter(dataDevolucaoPrevista.toLocalDate())) {
                    long diasAtraso = hoje.toEpochDay() - dataDevolucaoPrevista.toLocalDate().toEpochDay();
                    statusData = VERMELHO + " (Atrasado: " + diasAtraso + " dias)" + RESET;
                } else if (hoje.plusDays(7).isAfter(dataDevolucaoPrevista.toLocalDate())) {
                    long diasRestantes = dataDevolucaoPrevista.toLocalDate().toEpochDay() - hoje.toEpochDay();
                    statusData = AMARELO + " (Vence em: " + diasRestantes + " dias)" + RESET;
                }

                System.out.printf("%-5d %-20s %-20s %-15s %-20s %-10d %s%n",
                        id, funcionario, epi, dataEmprestimo, dataDevolucaoPrevista, quantidade, statusData);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar empréstimos ativos: " + e.getMessage() + RESET);
        }
    }

    /**
     * Lista empréstimos que estão próximos do vencimento (7 dias ou menos)
     */
    public static void listarEmprestimosProximosVencimento() {
        System.out.println("\n" + AZUL + "--- EMPRÉSTIMOS PRÓXIMOS DO VENCIMENTO ---" + RESET);
        String sql = "SELECT e.id, f.nome AS funcionario, ep.nome AS epi, e.data_emprestimo, " +
                "e.data_devolucao_prevista, e.quantidade, " +
                "DATEDIFF(e.data_devolucao_prevista, CURDATE()) AS dias_restantes " +
                "FROM emprestimo e " +
                "JOIN funcionario f ON e.id_funcionario = f.id " +
                "JOIN epi ep ON e.id_epi = ep.id " +
                "WHERE e.status = 'Ativo' " +
                "AND DATEDIFF(e.data_devolucao_prevista, CURDATE()) BETWEEN 0 AND 7 " +
                "ORDER BY e.data_devolucao_prevista";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-20s %-15s %-20s %-10s %-15s%n",
                    "ID", "Funcionário", "EPI", "Data Empréstimo", "Devolução Prevista", "Quantidade", "Dias Restantes");
            System.out.println("--------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String funcionario = rs.getString("funcionario");
                String epi = rs.getString("epi");
                Date dataEmprestimo = rs.getDate("data_emprestimo");
                Date dataDevolucaoPrevista = rs.getDate("data_devolucao_prevista");
                int quantidade = rs.getInt("quantidade");
                int diasRestantes = rs.getInt("dias_restantes");

                String statusDias = diasRestantes <= 0 ?
                        VERMELHO + "VENCIDO" + RESET :
                        AMARELO + diasRestantes + " dias" + RESET;

                System.out.printf("%-5d %-20s %-20s %-15s %-20s %-10d %-15s%n",
                        id, funcionario, epi, dataEmprestimo, dataDevolucaoPrevista, quantidade, statusDias);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar empréstimos próximos do vencimento: " + e.getMessage() + RESET);
        }
    }

    // ========== MÉTODOS PARA DEVOLUÇÕES ==========

    /**
     * Registra a devolução de um empréstimo
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void registrarDevolucao(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- REGISTRAR DEVOLUÇÃO ---" + RESET);

        try {
            // Listar empréstimos ativos
            System.out.println("\n" + AZUL + "EMPRÉSTIMOS ATIVOS:" + RESET);
            listarEmprestimosAtivos();

            System.out.print("\nDigite o ID do empréstimo: ");
            int idEmprestimo = scanner.nextInt();
            scanner.nextLine();

            LocalDate dataDevolucao = LocalDate.now();

            String sql = "UPDATE emprestimo SET data_devolucao_real = ?, status = 'Devolvido' WHERE id = ? AND status = 'Ativo'";

            try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setString(1, dataDevolucao.toString());
                stmt.setInt(2, idEmprestimo);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    // Recuperar quantidade e ID do EPI para atualizar o estoque
                    String sqlInfo = "SELECT id_epi, quantidade FROM emprestimo WHERE id = ?";
                    try (PreparedStatement stmtInfo = conexao.prepareStatement(sqlInfo)) {
                        stmtInfo.setInt(1, idEmprestimo);
                        ResultSet rs = stmtInfo.executeQuery();

                        if (rs.next()) {
                            int idEPI = rs.getInt("id_epi");
                            int quantidade = rs.getInt("quantidade");

                            // Atualizar estoque
                            atualizarEstoqueEPI(idEPI, quantidade);
                        }
                    }

                    System.out.println(VERDE + "Devolução registrada com sucesso!" + RESET);

                    // Verifica se houve atraso
                    String sqlAtraso = "SELECT DATEDIFF(?, data_devolucao_prevista) AS dias_atraso " +
                            "FROM emprestimo WHERE id = ?";
                    try (PreparedStatement stmtAtraso = conexao.prepareStatement(sqlAtraso)) {
                        stmtAtraso.setString(1, dataDevolucao.toString());
                        stmtAtraso.setInt(2, idEmprestimo);
                        ResultSet rsAtraso = stmtAtraso.executeQuery();

                        if (rsAtraso.next()) {
                            int diasAtraso = rsAtraso.getInt("dias_atraso");
                            if (diasAtraso > 0) {
                                System.out.println(VERMELHO + "ATENÇÃO: Devolução com atraso de " + diasAtraso + " dias!" + RESET);
                            }
                        }
                    }
                } else {
                    System.out.println(VERMELHO + "Nenhum empréstimo ativo encontrado com o ID: " + idEmprestimo + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao registrar devolução: " + e.getMessage() + RESET);
            scanner.nextLine();
        }
    }

    // ========== MÉTODOS PARA RELATÓRIOS ==========

    /**
     * Lista todos os EPIs disponíveis (com quantidade > 0)
     */
    public static void listarEPIsDisponiveis() {
        System.out.println("\n" + AZUL + "--- EPIs DISPONÍVEIS ---" + RESET);
        String sql = "SELECT * FROM epi WHERE quantidade > 0 ORDER BY nome";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-30s %-20s %-15s %-10s%n", "ID", "Nome", "Descrição", "Validade", "Quantidade");
            System.out.println("--------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                Date validade = rs.getDate("validade");
                int quantidade = rs.getInt("quantidade");

                // Verifica se o EPI está próximo da validade (30 dias ou menos)
                LocalDate hoje = LocalDate.now();
                LocalDate dataValidade = validade.toLocalDate();
                long diasRestantes = dataValidade.toEpochDay() - hoje.toEpochDay();

                String statusValidade = "";
                if (diasRestantes <= 0) {
                    statusValidade = VERMELHO + "VENCIDO" + RESET;
                } else if (diasRestantes <= 30) {
                    statusValidade = AMARELO + "Vence em " + diasRestantes + " dias" + RESET;
                }

                System.out.printf("%-5d %-30s %-20s %-15s %-10d %s%n",
                        id, nome, descricao, validade, quantidade, statusValidade);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar EPIs disponíveis: " + e.getMessage() + RESET);
        }
    }

    /**
     * Lista todos os EPIs que estão emprestados
     */
    public static void listarEPIsEmprestados() {
        System.out.println("\n" + AZUL + "--- EPIs EMPRESTADOS ---" + RESET);
        String sql = "SELECT ep.id, ep.nome, ep.descricao, SUM(e.quantidade) AS total_emprestado " +
                "FROM epi ep " +
                "JOIN emprestimo e ON ep.id = e.id_epi " +
                "WHERE e.status = 'Ativo' " +
                "GROUP BY ep.id, ep.nome, ep.descricao " +
                "ORDER BY ep.nome";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-30s %-20s %-15s%n", "ID", "Nome", "Descrição", "Quantidade Emprestada");
            System.out.println("----------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int totalEmprestado = rs.getInt("total_emprestado");

                System.out.printf("%-5d %-30s %-20s %-15d%n", id, nome, descricao, totalEmprestado);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao listar EPIs emprestados: " + e.getMessage() + RESET);
        }
    }

    /**
     * Mostra o histórico de empréstimos por funcionário (com entrada do usuário)
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void historicoEmprestimosPorFuncionario(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- HISTÓRICO DE EMPRÉSTIMOS POR FUNCIONÁRIO ---" + RESET);
        listarFuncionarios();

        System.out.print("\nDigite o ID do funcionário: ");

        try {
            int idFuncionario = scanner.nextInt();
            scanner.nextLine();
            historicoEmprestimosPorFuncionario(idFuncionario);
        } catch (Exception e) {
            System.out.println(VERMELHO + "ID inválido! Digite um número." + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Mostra o histórico de empréstimos por funcionário (sobrecarga)
     * @param idFuncionario ID do funcionário
     */
    private static void historicoEmprestimosPorFuncionario(int idFuncionario) {
        String sql = "SELECT e.id, ep.nome AS epi, e.data_emprestimo, e.data_devolucao_prevista, " +
                "e.data_devolucao_real, e.quantidade, e.status, " +
                "DATEDIFF(IFNULL(e.data_devolucao_real, CURDATE()), e.data_devolucao_prevista) AS dias_atraso " +
                "FROM emprestimo e " +
                "JOIN epi ep ON e.id_epi = ep.id " +
                "WHERE e.id_funcionario = ? " +
                "ORDER BY e.data_emprestimo DESC";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n" + AZUL + "HISTÓRICO DE EMPRÉSTIMOS:" + RESET);
            System.out.printf("%-5s %-20s %-15s %-20s %-20s %-10s %-10s %-15s%n",
                    "ID", "EPI", "Data Empréstimo", "Devolução Prevista",
                    "Devolução Real", "Quantidade", "Status", "Atraso");
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String epi = rs.getString("epi");
                Date dataEmprestimo = rs.getDate("data_emprestimo");
                Date dataDevolucaoPrevista = rs.getDate("data_devolucao_prevista");
                Date dataDevolucaoReal = rs.getDate("data_devolucao_real");
                int quantidade = rs.getInt("quantidade");
                String status = rs.getString("status");
                int diasAtraso = rs.getInt("dias_atraso");

                // Formata o status com cores
                String statusFormatado = status.equals("Ativo") ? VERDE + status + RESET : CIANO + status + RESET;

                // Formata o atraso
                String atrasoFormatado = "";
                if (diasAtraso > 0) {
                    atrasoFormatado = VERMELHO + diasAtraso + " dias" + RESET;
                } else if (status.equals("Ativo") && diasAtraso < 0) {
                    atrasoFormatado = AMARELO + "Faltam " + (-diasAtraso) + " dias" + RESET;
                }

                System.out.printf("%-5d %-20s %-15s %-20s %-20s %-10d %-10s %-15s%n",
                        id, epi, dataEmprestimo, dataDevolucaoPrevista,
                        dataDevolucaoReal != null ? dataDevolucaoReal : "N/A",
                        quantidade, statusFormatado, atrasoFormatado);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao buscar histórico: " + e.getMessage() + RESET);
        }
    }

    /**
     * Mostra o histórico de empréstimos por EPI (com entrada do usuário)
     * @param scanner Objeto Scanner para entrada de dados
     */
    public static void historicoEmprestimosPorEPI(Scanner scanner) {
        System.out.println("\n" + AZUL + "--- HISTÓRICO DE EMPRÉSTIMOS POR EPI ---" + RESET);
        listarEPIs();

        System.out.print("\nDigite o ID do EPI: ");

        try {
            int idEPI = scanner.nextInt();
            scanner.nextLine();
            historicoEmprestimosPorEPI(idEPI);
        } catch (Exception e) {
            System.out.println(VERMELHO + "ID inválido! Digite um número." + RESET);
            scanner.nextLine();
        }
    }

    /**
     * Mostra o histórico de empréstimos por EPI (sobrecarga)
     * @param idEPI ID do EPI
     */
    private static void historicoEmprestimosPorEPI(int idEPI) {
        String sql = "SELECT e.id, f.nome AS funcionario, e.data_emprestimo, e.data_devolucao_prevista, " +
                "e.data_devolucao_real, e.quantidade, e.status, " +
                "DATEDIFF(IFNULL(e.data_devolucao_real, CURDATE()), e.data_devolucao_prevista) AS dias_atraso " +
                "FROM emprestimo e " +
                "JOIN funcionario f ON e.id_funcionario = f.id " +
                "WHERE e.id_epi = ? " +
                "ORDER BY e.data_emprestimo DESC";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idEPI);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n" + AZUL + "HISTÓRICO DE EMPRÉSTIMOS:" + RESET);
            System.out.printf("%-5s %-20s %-15s %-20s %-20s %-10s %-10s %-15s%n",
                    "ID", "Funcionário", "Data Empréstimo", "Devolução Prevista",
                    "Devolução Real", "Quantidade", "Status", "Atraso");
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String funcionario = rs.getString("funcionario");
                Date dataEmprestimo = rs.getDate("data_emprestimo");
                Date dataDevolucaoPrevista = rs.getDate("data_devolucao_prevista");
                Date dataDevolucaoReal = rs.getDate("data_devolucao_real");
                int quantidade = rs.getInt("quantidade");
                String status = rs.getString("status");
                int diasAtraso = rs.getInt("dias_atraso");

                // Formata o status com cores
                String statusFormatado = status.equals("Ativo") ? VERDE + status + RESET : CIANO + status + RESET;

                // Formata o atraso
                String atrasoFormatado = "";
                if (diasAtraso > 0) {
                    atrasoFormatado = VERMELHO + diasAtraso + " dias" + RESET;
                } else if (status.equals("Ativo") && diasAtraso < 0) {
                    atrasoFormatado = AMARELO + "Faltam " + (-diasAtraso) + " dias" + RESET;
                }

                System.out.printf("%-5d %-20s %-15s %-20s %-20s %-10d %-10s %-15s%n",
                        id, funcionario, dataEmprestimo, dataDevolucaoPrevista,
                        dataDevolucaoReal != null ? dataDevolucaoReal : "N/A",
                        quantidade, statusFormatado, atrasoFormatado);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao buscar histórico: " + e.getMessage() + RESET);
        }
    }

    /**
     * Verifica e lista EPIs que estão próximos da validade (30 dias ou menos)
     */
    public static void verificarEPIsProximosValidade() {
        System.out.println("\n" + AZUL + "--- EPIs COM VALIDADE PRÓXIMA (30 dias ou menos) ---" + RESET);
        String sql = "SELECT id, nome, descricao, validade, quantidade, " +
                "DATEDIFF(validade, CURDATE()) AS dias_restantes " +
                "FROM epi " +
                "WHERE validade BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                "ORDER BY validade";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-30s %-20s %-15s %-10s %-15s%n",
                    "ID", "Nome", "Descrição", "Validade", "Quantidade", "Dias Restantes");
            System.out.println("----------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                Date validade = rs.getDate("validade");
                int quantidade = rs.getInt("quantidade");
                int diasRestantes = rs.getInt("dias_restantes");

                String statusDias = diasRestantes <= 7 ?
                        VERMELHO + diasRestantes + " dias" + RESET :
                        AMARELO + diasRestantes + " dias" + RESET;

                System.out.printf("%-5d %-30s %-20s %-15s %-10d %-15s%n",
                        id, nome, descricao, validade, quantidade, statusDias);
            }
        } catch (SQLException e) {
            System.err.println(VERMELHO + "Erro ao verificar EPIs próximos da validade: " + e.getMessage() + RESET);
        }
    }
}
