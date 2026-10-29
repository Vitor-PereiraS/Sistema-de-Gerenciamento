package view;

import java.util.List;
import java.util.Scanner;

import entidades.Department;
import entidades.dto.DepartmentDto;
import service.DepartmentService;


public class DepartmentView {

	public static void MostrarMenuSeller(Scanner leitura, int menu, DepartmentService departmentService) {
		String menuDepartamentos = "\n"
				+"********************* \n" 
				+"    Departamentos     \n" 
				+"********************* \n"
				+ "Digite: \n" 
				+ "1 para encontrar um departamento. \n" 
				+ "2 para listar os departamentos. \n"
				+ "3 para atualizar um departamento. \n"
				+ "4 para inserir um departamento. \n" 
				+ "5 para deletar um departamento. \n" 
				+ "6 para Voltar. \n" 
				+ "0 para Sair. \n";

		System.out.println(menuDepartamentos);
		int opcaoDepartamentos = leitura.nextInt();

		switch (opcaoDepartamentos) {
		case 0:
			menu = 0;
			break;

		case 1:
			System.out.println("Digite o ID do departamento que deseja buscar: ");
			int idDepartamento = leitura.nextInt();

			System.out.println(departmentService.findById(idDepartamento));

			break;
		case 2:
			List<DepartmentDto> lista = departmentService.findAll();

			for (DepartmentDto department: lista) {
				System.out.println(department);
			}
			break;
		case 3:
			atualizarDepartamento(leitura, departmentService);
			break;

		case 4:

			InserirDepartamento(leitura, departmentService);
			break;

		case 5:

			System.out.println("Digite o id do departamento que deseja deletar: ");
			idDepartamento = leitura.nextInt();

			departmentService.deleteById(idDepartamento);

			break;
			
		case 6:
			menu = 1;
			
			break;
		default:
			break;

		}
	}

	private static void InserirDepartamento(Scanner leitura, DepartmentService departmentService) {
		leitura.nextLine();
		System.out.println("Digite o novo nome para o departamento: ");
		String nome = leitura.nextLine();
		DepartmentDto dep = new DepartmentDto(null, nome);
		
		departmentService.insert(dep);
		
	}

	private static void atualizarDepartamento(Scanner leitura, DepartmentService departmentService) {
		System.out.println("Digite o ID do departamento que deseja atualizar: ");
		int idDepartment = leitura.nextInt();
		leitura.nextLine();

		DepartmentDto department = departmentService.findById(idDepartment);
		System.out.println(department);
		System.out.println();

		System.out.println("Digite o novo nome para o departamento: ");
		String nome = leitura.nextLine();
		department.setName(nome);
//		departmentService.update(department);
	}

}
