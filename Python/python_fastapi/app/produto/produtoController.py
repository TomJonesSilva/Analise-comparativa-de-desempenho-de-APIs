from fastapi import APIRouter
from app.schemas import CreateProdutoDto
from app.produto.produtoService import (
    criar_produto,
    listar_todos_produtos,
    buscar_por_categoria,
)

router = APIRouter(prefix="/produtos")

@router.post("/")
def create(dto: CreateProdutoDto):
    return criar_produto(dto)

@router.get("/")
def find_all():
    return listar_todos_produtos()

@router.get("/{categoria}")
def find_categoria(categoria: str):
    return buscar_por_categoria(categoria)
