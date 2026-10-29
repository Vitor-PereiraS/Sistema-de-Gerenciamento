package programa;

import java.util.Scanner;

import service.DepartmentService;
import service.SellerService;
import view.Menu;

public class programa {

	public static void main(String[] args) {
		
		SellerService sellerService = new SellerService();
		 DepartmentService departmentService = new DepartmentService();
		 Scanner leitura = new Scanner(System.in);
		 int menu = 3;
		
		 Menu.mostrarMenu(leitura, menu, sellerService, departmentService);
		 System.out.println("Fim do programa");

	}

}
