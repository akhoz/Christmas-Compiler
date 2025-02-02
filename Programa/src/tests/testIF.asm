.data
newLine: .asciiz "\n"
string0: .asciiz " + "
string1: .asciiz " = "
string2: .asciiz " "
string3: .asciiz " "
string4: .asciiz "hola mundo"
string7: .asciiz "Hola Mundo, Soy un IF"
.text
.globl main
main:
j global
func:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
lw $ra, 0($sp)
jr $ra
global:
subu $sp, $sp, 12
sw $ra, 0($sp)
li $s7, 3
sw $s7, 4($sp)
li $s7, 3
sw $s7, 8($sp)
lw $t0, 4($sp)
move $a0, $t0
li $v0, 1
syscall
la $a0,string0
li $v0, 4
syscall
lw $t1, 8($sp)
move $a0, $t1
li $v0, 1
syscall
la $a0,string1
li $v0, 4
syscall
lw $t2, 4($sp)
lw $t3, 8($sp)
add $t4, $t2, $t3
move $a0, $t4
li $v0, 1
syscall
li $v0, 4
la $a0, newLine
syscall
li.s $f12,3.14
li $v0, 2
syscall
la $a0,string2
li $v0, 4
syscall
li $a0, 3
li $v0, 1
syscall
la $a0,string3
li $v0, 4
syscall
la $a0,string4
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
lw $t2, 4($sp)
li $t3, 2
blt $t2, $t3, setTrue5
li $t5, 0
comparisonEnd5:
nextCondition1:
li $t2 0
beq $t5, $t2, nextCondition2
lw $t0, 4($sp)
move $a0, $t0
li $v0, 1
syscall
li $v0, 4
la $a0, newLine
syscall
nextCondition2:
ifEnd2:
lw $t1, 4($sp)
li $t2, 2
bgt $t1, $t2, setTrue6
li $t3, 0
comparisonEnd6:
nextCondition3:
li $t1 0
beq $t3, $t1, nextCondition4
la $a0,string7
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
nextCondition4:
ifEnd4:
addu $sp, $sp, 12
j finalCodigo
setTrue5:
li $t5, 1
j comparisonEnd5
setTrue6:
li $t3, 1
j comparisonEnd6
finalCodigo:
li $v0, 10
syscall
