package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import entidades.dto.DepartmentDto;
import entidades.dto.SellerDto;
import service.DepartmentService;
import service.SellerService;

public class SellerView {

	public static void MostrarMenuSeller(Scanner leitura, int menu, SellerService sellerService, DepartmentService departmentService) {
		String menuVendedores = "\n" + "********************* \n" + "      Vendedores      \n"
				+ "********************* \n" + "Digite: \n" + "1 para encontrar um vendedor. \n"
				+ "2 para listar os vendedores. \n" + "3 para listar os vendedores por departamento. \n"
				+ "4 para atualizar um vendedor. \n" + "5 para inserir um vendedor. \n"
				+ "6 para deletar um vendedor. \n" + "7 para Voltar. \n" + "0 para Sair. \n";

		System.out.println(menuVendedores);
		int opcaoVendedores = leitura.nextInt();

		switch (opcaoVendedores) {
		case 0:
			menu = 0;
			break;

		case 1:
			System.out.println("Digite o ID do vendedor que deseja buscar ");
			int idVendedor = leitura.nextInt();

			System.out.println(sellerService.findById(idVendedor));

			break;
		case 2:
			List<SellerDto> listaVendedores = sellerService.findAll();

			for (SellerDto seller : listaVendedores) {
				System.out.println(seller);
			}
			break;
		case 3:
			System.out.println("Digite o id do departamento que deseja buscar os vendedores ");
			int idDepartment = leitura.nextInt();
			DepartmentDto dep = departmentService.findById(idDepartment);

			listaVendedores = sellerService.findByDepartment(dep);

			System.out.println(dep.getName());
			for (SellerDto seller : listaVendedores) {
				System.out.println(seller);
			}
			break;

		case 4:

			atualizarVendedor(leitura, menu, sellerService, departmentService);
			break;

		case 5:

			InserirVendedor(leitura, menu, sellerService, departmentService);

			break;

		case 6:

			System.out.println("Digite o id do vendedor que deseja deletar");
			idVendedor = leitura.nextInt();

			sellerService.deleteById(idVendedor);
			break;

		case 7:
			menu = 1;
			break;
		default:
			break;

		}
	}

	private static void InserirVendedor(Scanner leitura, int menu, SellerService sellerService, DepartmentService departmentService) {

		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		leitura.nextLine();
		System.out.println("Digite o nome do vendedor: ");
		String nome = leitura.nextLine();

		System.out.println("Digite o email do vendedor: ");
		String email = leitura.nextLine();

		System.out.println("Digite a data de nascimento (dd/mm/aaaa) ");
		LocalDate dataNascimento = LocalDate.parse(leitura.nextLine(), formatador);

		System.out.println("Digite o sal�rio");
		double salario = leitura.nextDouble();

		System.out.println("Digite o id do Departamento: ");
		int idDepartment = leitura.nextInt();

		SellerDto seller = new SellerDto(null, nome, email, dataNascimento, salario, departmentService.findById(idDepartment));
		sellerService.insert(seller);
	}

	private static void atualizarVendedor(Scanner leitura, int menu, SellerService sellerService,
			DepartmentService departmentService) {
		System.out.println("Digite o ID do vendedor que deseja atualizar: ");
		int idVendedor = leitura.nextInt();
		leitura.nextLine();

		SellerDto seller = sellerService.findById(idVendedor);
		System.out.println(seller);
		System.out.println();

		if (seller != null) {

			String menuAtualizarVendedores = 
					"********************* \n" 
				+ 	" Atualizar Vendedores \n"
				+	"********************* \n" 
				+ 	"Digite: \n"
				+ 	"1 para atualizar o nome. \n"
				+ 	"2 para atualizar o email. \n"
				+ 	"3 para atualizar a data de nascimento. \n"
				+ 	"4 para atualizar o salario. \n" 
				+ 	"5 para Voltar. \n" + "0 para Sair. \n";

			System.out.println(menuAtualizarVendedores);
			int opcaoAtualizarVendedor = leitura.nextInt();
			leitura.nextLine();

			switch (opcaoAtualizarVendedor) {
			case 0:
				menu = 0;
				break;
			case 1:
				System.out.println("Digite o novo nome: ");
				String nomeVendedor = leitura.nextLine();

				seller.setName(nomeVendedor);
//				sellerService.update(seller);
				System.out.println();

				break;
			case 2:
				System.out.println("Digite o novo email: ");
				String email = leitura.nextLine();

				seller.setEmail(email);
//				sellerService.update(seller);
				System.out.println();

				break;
			case 3:
				DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

				System.out.println("Digite a data de nascimento (dd/mm/aaaa): ");
				LocalDate dataNascimento = LocalDate.parse(leitura.nextLine(), formatador);

				seller.setBirthDate(dataNascimento);
//				sellerService.update(seller);
				System.out.println();

				break;
			case 4:

				System.out.println("Digite o novo sal�rio: ");
				double salario = leitura.nextDouble();

				seller.setBaseSalary(salario);
//				sellerService.update(seller);
				System.out.println();
				break;
			case 5:
				menu = 2;
				break;
			default:
				break;
			}
		}
	}

}
