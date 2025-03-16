package com.example.wipmobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.wipmobile.R
import kotlin.math.truncate

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModelImage(path: String?, showStub: Boolean, modifier: Modifier = Modifier) {
    if (path != null) {
        GlideImage(
            model=path,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier.width(100.dp).height(100.dp).clip(RoundedCornerShape(16.dp)),
        )
    } else if (showStub) {
        Image(
            painter = painterResource(R.drawable.models_default_picture),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier.width(100.dp).height(100.dp).clip(RoundedCornerShape(16.dp)),
        )
    }
}


@Composable
fun ModelStatus(statusName: String?, modifier: Modifier = Modifier) {
    val name = statusName?: "Статус не определен"
    Box(
        modifier = Modifier.background(color= Color.LightGray, shape = RoundedCornerShape(10.dp)).padding(5.dp)
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = modifier
        )
    }
}

@Composable
fun ModelHoursSpend(hours: Double, fontSize: TextUnit = 12.sp) {
    val _hours = truncate(hours)
    val humanHours = truncate(_hours).toInt().toString()
    val humanMinutes = truncate((hours - _hours) * 60).toInt().toString()
    return Column {
        Text(text="$humanHours ч. $humanMinutes м", fontSize = fontSize)
    }
}


@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    error: String,
    clearError: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            clearError()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    clearError()
                }
            ) {
                Text(text = stringResource(R.string.ok_btn_label))
            }
        },
        title = {
            Text(
                text= stringResource(R.string.error_dialog_title),
                fontSize = 18.sp
            )
        },
        text = {
            Text(error)
        }
    )
}