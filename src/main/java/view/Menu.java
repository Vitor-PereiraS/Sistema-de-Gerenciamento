package view;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import exception.DbIntegrityException;
import service.DepartmentService;
import service.SellerService;

public class Menu {

	public static void mostrarMenu(Scanner leitura, int menu, SellerService sellerService,
			DepartmentService departmentService) {

		try {
			while (menu != 0) {
				String menu1Texto = "\n"
						+ "********************* \n" 
						+ "        Menu          \n" 
						+ "********************* \n"
						+ "Digite: \n" + "1 para Vendedores. \n" 
						+ "2 para Departamentos. \n" 
						+ "0 para Sair. \n";

				System.out.println(menu1Texto);
				int opcao = leitura.nextInt();

				switch (opcao) {
				case 0:
					menu = 0;
					break;
				case 1:
					SellerView.MostrarMenuSeller(leitura, menu, sellerService, departmentService);
					break;
				case 2:
					DepartmentView.MostrarMenuSeller(leitura, menu, departmentService);
					break;
				default:
					break;
				}
			}
		} catch (DbIntegrityException e) {
			System.out.println(e.getMessage());
			Menu.mostrarMenu(leitura, menu, sellerService, departmentService);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			Menu.mostrarMenu(leitura, menu, sellerService, departmentService);
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			Menu.mostrarMenu(leitura, menu, sellerService, departmentService);
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
			Menu.mostrarMenu(leitura, menu, sellerService, departmentService);
		}
	}
}
