/* 
Ankan Dash 
CS610 Data Structures and Algorithms
NJIT ID 31520550 
UCID ad892
PRP - Hash Table
*/
#include <iostream>
#include <fstream>
#include <string.h>
#include <stdio.h>
#include <vector>
using namespace std;
int fm; 
class hash_table_0550{
private:
	int index; 
	string ref = " "; 
	string word; 
public:
	hash_table_0550() { index = 0; word = "_"; }
	hash_table_0550(int i, string r){ index = i; word = r; }
	void initEntry(int idx) { index = idx; word = " "; }
	void setIndex (int n) { index = n; }
	void setRef (int n) {ref = to_string(n); }
	void setRef (string n) { ref = n; }
	int getIndex () { return index; }
	string getRef () { return ref; }
	string getVal() { return word; }
};
class HashTable_{
private:
	int collisions; 
public:
	vector<hash_table_0550> table;
	HashTable_() {collisions = 0;}
	void createTable(int n){ 
		table.resize(n); 
		for (int i = 0; i < n; i++){
			table[i].initEntry(i);
		}
	}
	string getTableVal(int idx) {return table[idx].getVal();}
	void insert(hash_table_0550 e) {table[e.getIndex()] = e;}
	void incC() {collisions++;}
	void resizeT(int n) { 
		int prevSize = table.size();
		table.resize(n);
		for (int i = prevSize; i < n; i++)
			table[i].initEntry(i);
	}
	int getTableSize() {return table.size(); }
	int getCollisons() { return collisions; }
};
class lexicon_{
public:
	HashTable_ T; 
	string A; 
	string defaultA; 
};
int HashBatch(lexicon_* L, ifstream &file);
int HashSearch(lexicon_* L, string w);
void HashCreate(lexicon_* L, int m);
void HashInsert(lexicon_* L, string w);
void HashDelete(lexicon_* L, string w);
void HashPrint(lexicon_* L);
bool HashEmpty(lexicon_ L); 
bool HashFull(lexicon_ L);

int main(int argc, char* argv[]){
	lexicon_ L;
	if (argc <= 0)
		cout << "Please try again!\n";
	else if (argc == 2){ 
		ifstream file;
		file.open(argv[1]);
		HashBatch(&L, file);
		file.close();
	}
	else 
		cout << "Enter using the correct format: Expected format is ./filename textfile.txt\n";
	return(0);
}
int HashBatch(lexicon_* L, ifstream &file){
	bool lexiconCreated = false; 
	if (!file){ 
		cout << "File cannot be opened!\n";
		return(1);
	}
	else { 
		string line, command = "";
		int c = 0;
		while (file >> line){
			if (c == 0) { 
				command = line;
				c++; 
				if (command == "13" && lexiconCreated){ 
					c = 0;
					HashPrint(L);
				}
			}
			else {
				c = 0;
				if (command == to_string(10) && lexiconCreated) { 
					HashInsert(L, line);
				}
				else if (command == to_string(11) && lexiconCreated) { 
					HashDelete(L, line);
				}
				else if (command == to_string(12) && lexiconCreated) { 
					int slot = HashSearch(L, line);
					if (slot != -1)
						cout << line << "	found at slot " << slot << endl;
					else
						cout << line << "	not found\n";
				}
				else if (command == to_string(14) && !lexiconCreated){ 
					lexiconCreated = true;
					HashCreate(L, stoi(line)); 
				}
			}
		}
	}
}
void HashCreate(lexicon_* L, int m){
	int newSize = 15 * m;
	L->T.createTable(m); 
	L->A.resize(newSize, ' '); 
	L->defaultA = L->A; 
}
void HashInsert(lexicon_* L, string w){
	int pIndex = 0; 
	int ASCII_VAL = 0;
	int h, h1;
	for (int i = 0; i < w.length(); i++) {
		ASCII_VAL += (int)w[i];
	}
	h1 = ASCII_VAL % L->T.getTableSize();
	h = (h1 + (pIndex * pIndex)) % L->T.getTableSize();	
	if (L->T.table[h].getVal() != " ") { 
		L->T.incC(); 
		bool noneLeft = true;
		while (pIndex < L->T.getTableSize()){
			pIndex++; 
			h = (h1 + (pIndex * pIndex)) % L->T.getTableSize(); 
			if (L->T.table[h].getVal() == " "){ 
				noneLeft = false;
				break;
			}
			else { 
				L->T.incC(); 
			}
		}
		if (noneLeft){
			cout << "Unable to insert " << w << ", probing index limit reached\n";
			return;
		}
	}
	w = w + '\0'; 
	int spot;
	for (int i = 0; i < L->A.length(); i++){
		if (L->A[i] == ' ') {
			spot = i;
			break;
		}
	}	
	hash_table_0550 nE(h, w);
	nE.setRef(spot);
	L->T.insert(nE);
	int j = 0; 
	if (w.length() <= L->A.length()) {
		for (int i = spot; i < L->A.length(); i++){
			L->A[i] = w[j];
			j++;
			if (j == w.length())
				break;
		}
	}
	else { 
		L->A.resize(L->A.length() + w.length(), ' '); 
		for (int i = spot; i < L->A.length(); i++){
			L->A[i] = w[j];
			j++;
			if (j == w.length())
				break;
		}
	}
}	
void HashDelete(lexicon_* L, string w){
	int idx = HashSearch(L, w); 
	if (idx != -1){
		int j = 0;
		int refIdx = stoi(L->T.table[idx].getRef());
		for (int i = refIdx; i < L->A.length(); i++){
			L->A[i] = '*';
			j++;
			if (j == w.length())
				break;
		}
		L->T.table[idx].setRef(" ");
		cout << w << "	deleted from slot " << idx << endl;
	}
}
int HashSearch(lexicon_* L, string w){
	w = w + '\0';
	for (int i = 0; i < L->T.getTableSize(); i++){
		if (L->T.table[i].getVal() == w) {
			return i; 
		}
	}
	return -1; 
}
bool HashEmpty(lexicon_ L){
	if (L.T.getTableSize() != 0){
		for (int i = 0; i < L.T.getTableSize(); i++){
			if (L.T.getTableVal(i) != " " || L.T.getTableVal(i) != "_") return false; 
		}
	}
	return true;
}
bool HashFull(lexicon_ L){
	for (int i = 0; i < L.T.getTableSize(); i++){
		if (L.T.getTableVal(i) == " " || L.T.getTableVal(i) == "_"){
			return false; 
		}
	}
	return true;
}
void HashPrint(lexicon_* L){
	cout <<	"\n  T			A: ";
	int spot = L->A.length() - 1;
	for (int i = 0; i < L->A.length(); i++){
		if (L->A[i] == ' ') {
			spot = i;
			break;
		}
	}
	for (int i = 0; i < L->A.length(); i++){
		if (L->A[i] == '\0')
			cout << '\\';
		else if (i == spot){ 
			cout << L->A[i] << endl;
			break;
		}
		else 
			cout << L->A[i];
	}
	for (int i = 0; i < L->T.getTableSize(); i++){
		cout << i << ": " << L->T.table[i].getRef() << endl;
	}
	cout << endl;
}