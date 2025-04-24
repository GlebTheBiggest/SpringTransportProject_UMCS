package org.example.app;

import org.example.app.dao.RentalDao;
import org.example.app.dao.UserDao;
import org.example.app.dao.VehicleDao;
import org.example.app.panels.GuestPanel;
import org.example.app.panels.UserPanel;
import org.example.app.panels.admin.panels.AdminPanel;
import org.example.app.utils.HibernateUtil;
import org.example.controllers.auth.Authentication;
import org.example.impls.repositories.RentalRepoImpl;
import org.example.impls.repositories.UserRepoImpl;
import org.example.impls.repositories.VehicleRepoImpl;
import org.example.impls.services.repoServices.RentalRepoServiceImpl;
import org.example.impls.services.repoServices.UserRepoServiceImpl;
import org.example.impls.services.repoServices.VehicleRepoServiceImpl;
import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.repositories.UserRepo;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.User;

import static org.example.app.utils.HibernateUtil.shutdown;

public class App {
    private static final UserDao userDao = new UserDao(HibernateUtil.getSessionFactory());
    private static final VehicleDao vehicleDao = new VehicleDao(HibernateUtil.getSessionFactory());
    private static final RentalDao rentalDao = new RentalDao(HibernateUtil.getSessionFactory());
    private static final UserRepo userRepo = new UserRepoImpl();
    private static final VehicleRepo vehicleRepo = new VehicleRepoImpl();
    private static final UserRepoService userService = new UserRepoServiceImpl(userRepo);
    private static final VehicleRepoService vehicleService = new VehicleRepoServiceImpl(vehicleRepo);
    private static final RentalRepo rentalRepo = new RentalRepoImpl();
    private static final RentalRepoService rentalService = new RentalRepoServiceImpl(rentalRepo);
    private static final Authentication auth = new Authentication(userService);
    private static final GuestPanel guestPanel = new GuestPanel(auth);

    public static void run() {
        while (true) {
            User USER = guestPanel.display();
            if (USER == null) {
                break;
            }
            if (USER.getRole().equalsIgnoreCase("ADMIN")) {
                new AdminPanel(USER, userService, vehicleService, rentalService, auth).start();
            }
            else {
                new UserPanel(USER, vehicleService, rentalService).start();
            }
        }
        shutdown();
        System.out.println("Exiting...");
    }
}
