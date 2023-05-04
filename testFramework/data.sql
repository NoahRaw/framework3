/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  ITU
 * Created: 25 avr. 2023
 */

---------------Donnee a inserer -------------
        CREATE SEQUENCE EmpSeq;
        CREATE TABLE Emp(
            idEmployer VARCHAR(100) NOT NULL DEFAULT 'EMP'||nextval('EmpSeq') PRIMARY KEY,
            nom VARCHAR(100) NOT NULL
        );