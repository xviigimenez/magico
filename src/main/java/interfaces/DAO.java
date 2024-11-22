/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;

/**
 *
 * @author Petenusso
 */
public interface DAO<T>{
   void inserir(T item);  
   void alterar(T item);
   void excluir(int id);
   T consultar(int id);
   List<T> listar();
}
