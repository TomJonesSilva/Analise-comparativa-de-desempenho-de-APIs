from pydantic import BaseModel
from typing import Optional, List
from datetime import date

class CreateProdutoDto(BaseModel):
    nome: str
    descricao: Optional[str] = None
    preco: float
    quantidade_em_estoque: Optional[int] = 0
    categoria: Optional[str] = None
    ativo: Optional[bool] = True
    fornecedor: Optional[str] = None
    codigo_barras: Optional[str] = None

class CreateVendaDto(BaseModel):
    produto_id: int
    quantidade: int
    preco_unitario: float
    data_venda: date

class VendaCalculoItem(BaseModel):
    preco: float
    quantidade: int

class CalculoProdutoBody(BaseModel):
    vendas: List[VendaCalculoItem]
    meses: int
    taxa: float
    iteracoes: int

class JurosCompostosBody(BaseModel):
    aporteMensal: float
    taxaMensal: float
    meses: int
    simulacoes: int
