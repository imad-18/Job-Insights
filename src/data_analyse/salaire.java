package data_analyse;

public class salaire {
    private String sal ;
    private int salaire1 ;
    private int salaire2 ;


    public salaire(String sal , int salaire1 , int salaire2 ) {
        this.sal = sal ;
        this.salaire1 = salaire1 ;
        this.salaire2 = salaire2 ;
    }

    public String getSalaire() {
        return sal;
    }
    public int getSalaire1() {
        return salaire1;
    }
    public int getSalaire2() {
        return salaire2;
    }

    public void setSalaire(String salaire) {
        this.sal = salaire;
    }
    public void setSalaire1(int salaire1) {
        this.salaire1 = salaire1;
    }
    public void setSalaire2(int salaire2) {
        this.salaire2 = salaire2;
    }

}
