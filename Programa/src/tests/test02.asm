.data
newLine: .asciiz "\n"
.text
.globl main
main:
j global
setTrue0:
li $f0, 1
j comparisonEnd0
setAndFalse1:
li $t3, 0
j logicalEnd1
finalCodigo:
li $v0, 10
syscall
