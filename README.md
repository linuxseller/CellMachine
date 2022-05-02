# CellMachine
this repositiry has java code that works as CellMashine itself and GUI extention
main part of this project is CellMachine.class. it is kind of library which is used for understanding how CellMashine works, creating yiur own user fruendly gui to iteract with cells and count some math(or other) problems.
## How to use CellMachine
```
CellMachine machine = new CellMachine(sizeOfSquare, startPositions);
```
startPositions is multidimensional array type - `{{x1,y1},{x2,y2},{x3,y3}...{xn,yn}}`
### Methods
```
update_thread()                       // updates field according to rules
parseRule(String text)                // parses rule  "b..../s..../"
clearField()                          // fills field with 0
setupField(int[][] positions)         // puts 1 for every point given in argument
printField(String dead, String alive) // prints field (0->dead, 1->alive)
```
### More about methods
some important info anoit method filling
setsetupField()
```
for(int[] i: positions) {
    cells[i[0]][i[1]]=1;
}
```
printField()
```
if(cells[x][y]==1) {
  System.out.print(alive+" ");
} else {
  System.out.print(dead+" ");
}
```
## Using CellMachine.class
```
int size = 50;
int[][] tst = {{2, 2}, {2, 1}, {2, 3}};
CellMachine machine = new CellMachine(size, tst);

machine.parseRule("b3/s23/");

for (int i = 0; i < 1; i++) {
    machine.update_thread();
    machine.print("X", "V");
}
```
expected output

```
X X X X X 
X X X X X 
X V V V X 
X X X X X 
X X X X X 

------------------------
```
