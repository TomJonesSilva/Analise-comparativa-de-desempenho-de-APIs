from fastapi import APIRouter
from app.schemas import CreateVendaDto, CalculoProdutoBody, JurosCompostosBody
from app.venda.vendaService import (
    criar_venda,
    listar_vendas,
    gerar_relatorio_mensal,
    calcular_metrica_produto,
    simular_juros_compostos
)

router = APIRouter(prefix="/vendas")

@router.post("/")
def create(dto: CreateVendaDto):
    return criar_venda(dto)

@router.get("/")
def find_all():
    return listar_vendas()

@router.get("/relatorio")
def relatorio():
    return gerar_relatorio_mensal()

@router.post("/calculo-produto")
def calculo_produto(body: CalculoProdutoBody):
    return calcular_metrica_produto(body)

@router.post("/juros-compostos")
def juros_compostos(body: JurosCompostosBody):
    return simular_juros_compostos(body)
