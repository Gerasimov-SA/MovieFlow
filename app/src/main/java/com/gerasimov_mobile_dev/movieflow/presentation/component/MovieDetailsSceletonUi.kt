package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MovieDetailsSkeletonHeader() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Фон-заглушка (backdrop)
        SkeletonBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            cornerRadius = 0.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Постер + правая колонка (заглушка)
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedSkeletonBox(
                modifier = Modifier

                    .width(120.dp)
                    .height(180.dp),
                cornerRadius = 12.dp
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                AnimatedSkeletonBox(
                    modifier = Modifier

                        .fillMaxWidth()
                        .height(32.dp),
                    cornerRadius = 4.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedSkeletonBox(
                    modifier = Modifier

                        .fillMaxWidth()
                        .height(20.dp),
                    cornerRadius = 4.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Рейтинг, длительность, возрастной рейтинг — ряд мелких блоков
                SkeletonRow(height = 20.dp, count = 3, gap = 6.dp, lastCornerRadius = 12.dp)
            }
        }
    }
}

@Composable
fun MovieDetailsSkeletonContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Жанры / страны (2 строки)
        repeat(2) {
            SkeletonRow(height = 24.dp, count = 1, gap = 0.dp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Заголовок «Описание»
        SkeletonRow(height = 28.dp, count = 1)
        Spacer(modifier = Modifier.height(8.dp))

        // Описание — несколько строк
        repeat(5) {
            SkeletonRow(height = 18.dp, count = 1)
            Spacer(modifier = Modifier.height(4.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Актёры
        SkeletonRow(height = 28.dp, count = 1) // заголовок
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonRow(height = 20.dp, count = 1) // строка актёров
        Spacer(modifier = Modifier.height(20.dp))

        // Трейлеры
        SkeletonRow(height = 28.dp, count = 1) // заголовок
        Spacer(modifier = Modifier.height(8.dp))
        repeat(3) {
            SkeletonRow(height = 22.dp, count = 1)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}