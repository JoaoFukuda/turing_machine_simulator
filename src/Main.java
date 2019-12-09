package src;

import java.io.File; // Para ler arquivos
import java.util.*; // Para pegar o tipo Map<>

class Instrucao
{
	public String entrada; // O que a máquina pode receber
	public String saida; // O que ela retorna (posição respectiva à entrada)
	public int direcao; // Direita = true, Esquerda = false
	public String prox_estado;

	public Instrucao(String in, String out, int dir, String prox)
	{
		entrada = in;
		saida = out;
		direcao = dir;
		prox_estado = prox;
	}
};

class Estado
{
	private Vector<Instrucao> instrucoes;

	public Estado(Scanner input)
	{
		instrucoes = new Vector<Instrucao>();
		while(input.hasNext())
		{
			String buffer = input.next();
			if(buffer.equalsIgnoreCase("estado"))
				break;
			String in = buffer;

			input.next(); // pegar o "escrever"
			String out = input.next();

			input.next(); // pegar o "mover"
			int dir = ((input.next().equalsIgnoreCase("direita")) ? 1 : -1);

			input.next(); // pegar o "estado"
			String next = input.next();

			instrucoes.add(new Instrucao(in, out, dir, next));
		}
	}

	public Instrucao InstComEntrada(String entrada)
	{
		System.out.println();
		for(Instrucao inst : instrucoes)
		{
			if(inst.entrada.equals(entrada))
				return inst;
		}
		return null;
	}
};

class MaquinaDeTuring
{
	private Map<String, Estado> estados;
	private Vector<String> fita;
	private String estado_atual;
	private int head;

	public boolean fim;

	private String SimboloEm(int index)
	{
		return ((index < 0 || index >= fita.size() || fita.get(index).equals("")) ? "nada" : fita.get(index));
	}

	public MaquinaDeTuring(Scanner input)
	{
		estado_atual = "";
		fim = false;
		estados = new HashMap<String, Estado>();

		input.next(); // pegar o primeiro "estado"

		while(input.hasNext())
		{
			String buffer = input.next();
			if(estado_atual.isEmpty())
				estado_atual = buffer;

			Estado aux = new Estado(input);

			estados.put(buffer, aux);
		}

		input.close();
	}

	public void AdicionarFita(Scanner input)
	{
		head = 0;
		fita = new Vector<String>();
		while(input.hasNextLine())
		{
			String buffer = input.nextLine();
			fita.add(buffer);
		}

		input.close();
	}

	public void Step()
	{
		if(head == -1)
		{
			head = 0;
			fita.add(0, "");
		}
		Instrucao inst = estados.get(estado_atual).InstComEntrada(SimboloEm(head));
		if(fita.size() == head)
			fita.add(inst.saida.equals("nada") ? "" : inst.saida);
		else
			fita.set(head, (inst.saida.equals("nada") ? "" : inst.saida));
		head += inst.direcao;
		estado_atual = inst.prox_estado;
		if(estado_atual.equals("parar"))
			fim = true;
	}

	public void PrintFita(int tamanho)
	{
		for(int aux = head - (tamanho / 2); aux < head + (tamanho / 2); aux++)
		{
			System.out.println(((aux == head) ? "> " : "  ") + SimboloEm(aux));
		}
	}
};

class Main
{
	public static void db()
	{
		System.out.println("ASDF");
	}

	public static void main(String args[])
	{
		try
		{
			System.out.println("Abrindo arquivo da MT" + args[0]);
			File file = new File(args[0]);
			Scanner in = new Scanner(file);

			MaquinaDeTuring mt = new MaquinaDeTuring(in);

			System.out.println("Abrindo arquivo da fita" + args[1]);
			file = new File(args[1]);
			in = new Scanner(file);
			mt.AdicionarFita(in);

			in = new Scanner(System.in);

			while(!mt.fim)
			{
				mt.PrintFita(20);
				mt.Step();
				System.out.println("\n[Enter] para avançar...");
				in.nextLine();
			}
			mt.PrintFita(20);

			in.close();
		} catch(Exception e)
		{
			System.out.println("Erro! " + e.getMessage());
		}
	}
};
