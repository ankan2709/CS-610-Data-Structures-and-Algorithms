/* 
Ankan Dash 0550
CS610 Data Structures and Algorithms
NJIT ID 31520550 
UCID ad892
Programming Project (PrP) - Hash Table
*/
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string.h>
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
	void hash_init_entry(int idx) { index = idx; word = " "; }
	void set_index (int n) { index = n; }
	void set_ref (int n) {ref = to_string(n); }
	void set_ref (string n) { ref = n; }
	int get_index () { return index; }
	string get_ref () { return ref; }
	string get_val() { return word; }
};

class hash_table{
private:
	int collisions; 
public:
	vector<hash_table_0550> table;
	hash_table() {collisions = 0;}
	void table_creation(int n){ 
		table.resize(n); 
		for (int i = 0; i < n; i++){
			table[i].hash_init_entry(i);
		}
	}
	string get_value(int idx) {return table[idx].get_val();}
	void insert(hash_table_0550 e) {table[e.get_index()] = e;}
	void incC() {collisions++;}
	void resizeT(int n) { 
		int prev_size = table.size();
		table.resize(n);
		for (int i = prev_size; i < n; i++)
			table[i].hash_init_entry(i);
	}
	int table_size() {return table.size(); }
	int get_collisions() { return collisions; }
};

class lexicon{
public:
	hash_table T; 
	string A; 
	string defaultA; 
};
int batch(lexicon* L, ifstream &file);
int search_hash(lexicon* L, string w);
void Create_hash(lexicon* L, int m);
void insert_hash(lexicon* L, string w);
void delete_hash(lexicon* L, string w);
void print_hash(lexicon* L);
bool empyt_hash(lexicon L); 
bool full_hash(lexicon L);

int main(int argc, char* argv[]){
	lexicon L;
	if (argc <= 0)
		cout << "Please try again!\n";
	else if (argc == 2){ 
		ifstream file;
		file.open(argv[1]);
		batch(&L, file);
		file.close();
	}
	else 
		cout << "Enter using the correct format: Expected format is ./filename textfile.txt\n";
	return(0);
}
int batch(lexicon* L, ifstream &file){
	bool lexicon_created = false; 
	if (!file){ 
		cout << "Cannot open the file\n";
		return(1);
	}
	else { 
		string line, command = "";
		int c = 0;
		while (file >> line){
			if (c == 0) { 
				command = line;
				c++; 
				if (command == "13" && lexicon_created){ 
					c = 0;
					print_hash(L);
				}
			}
			else {
				c = 0;
				if (command == to_string(10) && lexicon_created) { 
					insert_hash(L, line);
				}
				else if (command == to_string(11) && lexicon_created) { 
					delete_hash(L, line);
				}
				else if (command == to_string(12) && lexicon_created) { 
					int slot = search_hash(L, line);
					if (slot != -1)
						cout << line << "	found at slot " << slot << endl;
					else
						cout << line << "	not found\n";
				}
				else if (command == to_string(14) && !lexicon_created){ 
					lexicon_created = true;
					Create_hash(L, stoi(line)); 
				}
			}
		}
	}
}

void Create_hash(lexicon* L, int m){
	int new_size = 15 * m;
	L->T.table_creation(m); 
	L->A.resize(new_size, ' '); 
	L->defaultA = L->A; 
}

void insert_hash(lexicon* L, string w){
	int p_index = 0; 
	int ASCII_VAL = 0;
	int h, h1;
	for (int i = 0; i < w.length(); i++) {
		ASCII_VAL += (int)w[i];
	}

	h1 = ASCII_VAL % L->T.table_size();
	h = (h1 + (p_index * p_index)) % L->T.table_size();	
	if (L->T.table[h].get_val() != " ") { 
		L->T.incC(); 
		bool noneLeft = true;
		while (p_index < L->T.table_size()){
			p_index++; 
			h = (h1 + (p_index * p_index)) % L->T.table_size(); 
			if (L->T.table[h].get_val() == " "){ 
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
	nE.set_ref(spot);
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

void delete_hash(lexicon* L, string w){
	int idx = search_hash(L, w); 
	if (idx != -1){
		int j = 0;
		int refIdx = stoi(L->T.table[idx].get_ref());
		for (int i = refIdx; i < L->A.length(); i++){
			L->A[i] = '*';
			j++;
			if (j == w.length())
				break;
		}
		L->T.table[idx].set_ref(" ");
		cout << w << "	deleted from slot " << idx << endl;
	}
}

int search_hash(lexicon* L, string w){
	w = w + '\0';
	for (int i = 0; i < L->T.table_size(); i++){
		if (L->T.table[i].get_val() == w) {
			return i; 
		}
	}
	return -1; 
}
bool empyt_hash(lexicon L){
	if (L.T.table_size() != 0){
		for (int i = 0; i < L.T.table_size(); i++){
			if (L.T.get_value(i) != " " || L.T.get_value(i) != "_") return false; 
		}
	}
	return true;
}
bool full_hash(lexicon L){
	for (int i = 0; i < L.T.table_size(); i++){
		if (L.T.get_value(i) == " " || L.T.get_value(i) == "_"){
			return false; 
		}
	}
	return true;
}

void print_hash(lexicon* L){
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
	for (int i = 0; i < L->T.table_size(); i++){
		cout << i << ": " << L->T.table[i].get_ref() << endl;
	}
	cout << endl;
}